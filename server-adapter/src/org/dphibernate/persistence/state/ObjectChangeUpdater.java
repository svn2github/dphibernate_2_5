package org.dphibernate.persistence.state;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.havingValue;
import static org.hamcrest.Matchers.is;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.dphibernate.core.IEntity;
import org.dphibernate.persistence.interceptors.IChangeMessageInterceptor;
import org.dphibernate.serialization.SerializerCache;
import org.hibernate.SessionFactory;
import org.hibernate.TypeMismatchException;

/**
 * This class is not thread-safe. Use with prototype scope.
 * 
 * @author owner
 * 
 */
public class ObjectChangeUpdater implements IObjectChangeUpdater
{

	public ObjectChangeUpdater(){}
	public ObjectChangeUpdater(SessionFactory sessionFactory,IProxyResolver proxyResolver,SerializerCache cache)
	{
		this.sessionFactory = sessionFactory;
		this.proxyResolver = proxyResolver;
		this.cache = cache;
	}
	public ObjectChangeUpdater(SessionFactory sessionFactory,IProxyResolver proxyResolver)
	{
		this(sessionFactory,proxyResolver,new SerializerCache());
	}
	@Resource
	private SessionFactory sessionFactory;

	@Resource
	private IProxyResolver proxyResolver;

	@Resource
	private SerializerCache cache;
	
	private IPrincipalProvider principalProvider;

	private Map<String,Set<ObjectChangeResult>> processedKeys = new HashMap<String,Set<ObjectChangeResult>>();

	// When creating a chain of entities, we only commit the very top level
	// and let hibernate do the rest
	private IEntity topLevelEntity;

	private HashMap<IEntity, ObjectChangeMessage> entitiesAwaitingCommit = new HashMap<IEntity, ObjectChangeMessage>();

	private List<? extends IChangeMessageInterceptor> postProcessors;

	private List<? extends IChangeMessageInterceptor> preProcessors;


	@SuppressWarnings("unchecked")
	public Set<ObjectChangeResult> update(ObjectChangeMessage changeMessage)
	{
		Set<ObjectChangeResult> result = processUpdate(changeMessage);
		return result;
	}


	protected void applyPostProcessors(ObjectChangeMessage changeMessage)
	{
		applyInterceptors(changeMessage, getPostProcessors());
	}


	protected void applyInterceptors(ObjectChangeMessage changeMessage, List<? extends IChangeMessageInterceptor> interceptors)
	{
		if (interceptors == null)
			return;
		
		for (IChangeMessageInterceptor interceptor : interceptors)
		{
			if (interceptor.appliesToMessage(changeMessage))
			{
				applyInterceptor(interceptor,changeMessage);
			}
		}
	}
	
	private void applyInterceptor(IChangeMessageInterceptor interceptor, ObjectChangeMessage changeMessage)
	{
		if (getPrincipalProvider() == null)
		{
			interceptor.processMessage(changeMessage, proxyResolver);
		} else if (getPrincipalProvider().isAnonymous())
		{
			interceptor.processMessage(changeMessage, proxyResolver);
		} else {
			interceptor.processMessage(changeMessage, proxyResolver, getPrincipalProvider().getPrincipal());
		}
	}
	private void applyPreProcessors(ObjectChangeMessage changeMessage) throws ObjectChangeAbortedException
	{
		applyInterceptors(changeMessage, getPreProcessors());
	}


	private Set<ObjectChangeResult> processUpdate(ObjectChangeMessage changeMessage)
	{
		Set<ObjectChangeResult> result = new HashSet<ObjectChangeResult>();
		if (changeMessage.getResult() != null)
		{
			// We've already processed this message.
			result.add(changeMessage.getResult());
			return result;
		}
		String changeMessageKey = changeMessage.getOwner().getKey();
		if (processedKeys.containsKey(changeMessageKey))
		{
			// WORKAROUND:
			// Sometimes we can get multiple instances of change messages
			// for new objects when adding to a collection.
			// The "CREATE" has already been processed, and the result is stored,
			// but needs to be set on other collection member messages
			result = processedKeys.get(changeMessageKey);
			if (result.size() == 1 && changeMessage.getResult() == null)
			{
				changeMessage.setResult(result.iterator().next());
				changeMessage.setCreatedEntity(proxyResolver.resolve(changeMessage.getOwner()));
			}
			return result;
		}
		processedKeys.put(changeMessageKey,result);
		if (!changeMessage.hasChanges() && !changeMessage.getIsDeleted())
			return result;
		IEntity entity = getEntity(changeMessage);
		if (changeMessage.getIsNew())
		{
			proxyResolver.addInProcessProxy(changeMessage.getOwner().getKey(), entity);
			if (topLevelEntity == null)
				topLevelEntity = entity;
		}
		if (entity == null)
		{
			if (changeMessage.getIsDeleted())
			{
				// Let's not stress too much if we can't find the entity -- we were gonna kill it anyway...
				return result;
			}
			throw new IllegalArgumentException("No entity found or created");
		}
		if (changeMessage.getIsDeleted())
		{
			sessionFactory.getCurrentSession().delete(entity);
			return result;
		}
		for (PropertyChangeMessage propertyChangeMessage : changeMessage.getChangedProperties())
		{
			IChangeUpdater updater = getPropertyChangeUpdater(propertyChangeMessage, entity, proxyResolver);
			
			// It's possible the updater could return null if the change message was invalid.
			if (updater != null)
			{
				result.addAll(updater.update());
			}
		}
		if (changeMessage.getIsNew())
		{
			if (entity == topLevelEntity)
			{
				Serializable pk = sessionFactory.getCurrentSession().save(entity);
				ObjectChangeResult messageResult = new ObjectChangeResult(entity.getClass(), changeMessage.getOwner().getProxyId(), pk);
				changeMessage.setResult(messageResult);
				result.add(messageResult);
				/*
				 * proxyResolver.removeInProcessProxy(changeMessage.getOwner()
				 * .getKey(), entity);
				 */
				for (Entry<IEntity, ObjectChangeMessage> entityAwaitingCommit : entitiesAwaitingCommit.entrySet())
				{
					IEntity proxy = entityAwaitingCommit.getKey();
					if (proxy.getEntityKey() != null)
					{
						ObjectChangeMessage dependantChangeMessage = entityAwaitingCommit.getValue();
						ObjectChangeResult dependentMessageResult = new ObjectChangeResult(dependantChangeMessage, proxy.getEntityKey());
						dependantChangeMessage.setResult(dependentMessageResult);
						result.add(dependentMessageResult);
						entitiesAwaitingCommit.remove(entityAwaitingCommit);
					}
				}
				topLevelEntity = null;
			} else
			{
				entitiesAwaitingCommit.put(entity, changeMessage);
			}

		} else
		{
			sessionFactory.getCurrentSession().update(entity);
		}
		invalidateCacheForObject(changeMessage, entity);
		return result;

	}


	private void invalidateCacheForObject(ObjectChangeMessage changeMessage, Object entity)
	{
		cache.invalidate(changeMessage, entity);
	}


	@Override
	public Set<ObjectChangeResult> update(List<ObjectChangeMessage> changeMessages)
	{
		// For debugging:
		// XStream xStream = new XStream();
		// System.out.println(xStream.toXML(changeMessages));
		
		List<ObjectChangeMessage> changeMessagesToProcess = new ArrayList<ObjectChangeMessage>(changeMessages);
		applyPreProcessors(changeMessages);
		
		// Update new items first
		List<ObjectChangeMessage> newObjects = filter(havingValue(on(ObjectChangeMessage.class).getIsNew(), is(true)), changeMessagesToProcess);
		UpdateDependencyResolver dependencyResolver = new UpdateDependencyResolver();
		dependencyResolver.addMessages(newObjects);
		List<ObjectChangeMessage> newMessagesOrderedByDependency = dependencyResolver.getOrderedList();
		Set<ObjectChangeResult> result = doUpdate(newMessagesOrderedByDependency);

		changeMessagesToProcess.removeAll(newObjects);
		result.addAll(doUpdate(changeMessagesToProcess));
		
		sessionFactory.getCurrentSession().flush();
		
		applyPostProcessors(changeMessages);
		return result;
	}


	private void applyPreProcessors(List<ObjectChangeMessage> changeMessages) throws ObjectChangeAbortedException
	{
		for (ObjectChangeMessage changeMessage:changeMessages)
		{
			applyPreProcessors(changeMessage);
		}
	}


	private void applyPostProcessors(List<ObjectChangeMessage> changeMessages)
	{
		for (ObjectChangeMessage changeMessage : changeMessages)
		{
			applyPostProcessors(changeMessage);
		}
	}
	


	private Set<ObjectChangeResult> doUpdate(List<ObjectChangeMessage> changeMessages)
	{
		Set<ObjectChangeResult> result = new HashSet<ObjectChangeResult>();
		for (ObjectChangeMessage message : changeMessages)
		{
			result.addAll(update(message));
		}
		
		return result;
	}


	private IChangeUpdater getPropertyChangeUpdater(PropertyChangeMessage propertyChangeMessage, IEntity entity, IProxyResolver proxyResolver2)
	{
		if (propertyChangeMessage instanceof CollectionChangeMessage)
		{
			return new CollectionChangeUpdater((CollectionChangeMessage) propertyChangeMessage, entity, proxyResolver2, this);
		} else
		{
			try
			{
				PropertyChangeUpdater updater = new PropertyChangeUpdater(propertyChangeMessage, entity, proxyResolver2);
				return updater;
			} catch (IllegalUpdateException e)
			{
				return null;
			}
			
		}
	}


	@SuppressWarnings("unchecked")
	private IEntity getEntity(ObjectChangeMessage changeMessage)
	{
		String className = changeMessage.getOwner().getRemoteClassName();
		Class<? extends IEntity> entityClass;
		try
		{
			entityClass = (Class<? extends IEntity>) Class.forName(className);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (changeMessage.getIsNew())
		{
			try
			{
				IEntity instance = entityClass.newInstance();
				changeMessage.setCreatedEntity(instance);
				return instance;
			} catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		} else
		{
			try
			{
				Serializable primaryKey = (Serializable) changeMessage.getOwner().getProxyId();
				if (primaryKey instanceof String)
				{
					primaryKey = Integer.parseInt((String) primaryKey);
				}
				Object entity = sessionFactory.getCurrentSession().get(entityClass, primaryKey);
				return (IEntity) entity;
			} catch (TypeMismatchException e)
			{
				e.printStackTrace();
				throw e;
			}
		}
	}


	public void setCache(SerializerCache cache)
	{
		this.cache = cache;
	}


	public SerializerCache getCache()
	{
		return cache;
	}


	@Override
	public List<ObjectChangeMessage> orderByDependencies(List<ObjectChangeMessage> objectChangeMessages)
	{
		return null;
	}


	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}


	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}


	public void setPreProcessors(List<? extends IChangeMessageInterceptor> preProcessors)
	{
		this.preProcessors = preProcessors;
	}


	public List<? extends IChangeMessageInterceptor> getPreProcessors()
	{
		return preProcessors;
	}


	public void setPostProcessors(List<? extends IChangeMessageInterceptor> postProcessors)
	{
		this.postProcessors = postProcessors;
	}


	public List<? extends IChangeMessageInterceptor> getPostProcessors()
	{
		return postProcessors;
	}
	public void setPrincipleProvider(IPrincipalProvider principalProvider)
	{
		this.principalProvider = principalProvider;
	}
	public IPrincipalProvider getPrincipalProvider()
	{
		return principalProvider;
	}

}
