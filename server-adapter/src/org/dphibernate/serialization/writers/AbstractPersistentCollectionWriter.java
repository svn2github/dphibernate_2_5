package org.dphibernate.serialization.writers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.dphibernate.core.HibernateProxyConstants;
import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
import org.dphibernate.serialization.CollectionProxyResolver;
import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.serialization.DiscriminatedCollectionProxyResolver;
import org.dphibernate.serialization.HibernateSerializer;
import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializationWriter;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.serialization.SerializationMode;
import org.dphibernate.serialization.SingleTypeCollectionProxyResolver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.dialect.Dialect;
import org.hibernate.event.EventSource;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

import flex.messaging.io.amf.ASObject;
/**
 * Writer for hibernate collections (ie., AbstractPersistenceCollections)
 * 
 * Returns List<Object> (as opposed to List<ASObject>)
 * as Object[] serializes as ASObject[], which cannot be added to
 * List<ASObject>
 * @author Marty Pitt
 *
 */
public class AbstractPersistentCollectionWriter extends AbstractSerialziationWriter implements ISerializationWriter<AbstractPersistentCollection,List<Object>>
{
	private final SessionFactory sessionFactory;
	private final SerializationMode serialzationMode;
	private final Dialect dialect;
	public AbstractPersistentCollectionWriter(IPropertySerializer serializer,
			SerializationMode serialzationMode,
			SessionFactory sessionFactory,
			Dialect dialect)
	{
		super(serializer);
		this.serialzationMode = serialzationMode;
		this.sessionFactory = sessionFactory;
		this.dialect = dialect;
	}
	@Override
	public List<Object> createStubValue(AbstractPersistentCollection source)
	{
		return new ArrayList<Object>();
	}
	@Override
	public void populateStub(AbstractPersistentCollection source, List<Object> result)
	{
		if (shouldProxyCollectionMembers(source))
		{
			// go load our Collection of dpHibernateProxy objects
			List<Object> proxies = getCollectionProxies(source);
			proxies = (List<Object>) serialize(proxies);
			result.addAll(proxies);
		} else
		{
			result.addAll(serializeActualCollectionMembers(source));
		}
	}
	/**
	 * Generates ASObjects for the actual members of the collection.
	 * If the collection wasn't initialized (ie., loaded from the database)
	 * we initialize it.
	 * @param collection
	 * @return
	 */
	private List<Object> serializeActualCollectionMembers(AbstractPersistentCollection collection)
	{
		List<Object> result = new ArrayList<Object>();
		if (!collection.wasInitialized())
		{
			collection.forceInitialization();
		}
		Iterator<Object> itr = collection.entries(null);
		while (itr.hasNext())
		{
			Object next = itr.next();
			Object newObj = serialize(next);
			result.add(newObj);
		}
		return result;
	}
	private boolean shouldProxyCollectionMembers(AbstractPersistentCollection collection)
	{
		return (!collection.wasInitialized() && serialzationMode != SerializationMode.EAGERLY_SERIALIZE);
	}
	private CollectionProxyResolver getCollectionProxyResolver(AbstractCollectionPersister collectionPersister, EntityPersister entityPersister)
	{
		if (entityPersister instanceof SingleTableEntityPersister)
		{
			if (((SingleTableEntityPersister) entityPersister).getDiscriminatorColumnName() != null)
			{
				return new DiscriminatedCollectionProxyResolver(dialect,(SingleTableEntityPersister) entityPersister,collectionPersister);
			}
		}
		return new SingleTypeCollectionProxyResolver(dialect, collectionPersister);
	}
	/**
	 * Builds a collection of lazy proxies for objects in the collection.
	 * Objects are not loaded from the database. 
	 * @param collection
	 * @return
	 */
	List<Object> getCollectionProxies(PersistentCollection collection)
	{
		try
		{
			EventSource session = (EventSource) sessionFactory.getCurrentSession();

			CollectionPersister persister = session.getFactory().getCollectionPersister(collection.getRole());
			if (persister instanceof AbstractCollectionPersister)
			{
				AbstractCollectionPersister collectionPersister = (AbstractCollectionPersister) persister;
				String className = collectionPersister.getElementType().getName();
				EntityPersister entityPersister = session.getFactory().getEntityPersister(className);
				CollectionProxyResolver collectionProxyResolver = getCollectionProxyResolver(collectionPersister, entityPersister);
				if (session instanceof Session)
				{
					List<IHibernateProxyDescriptor> proxyDescriptors = collectionProxyResolver.getCollectionProxies(session, collection);

					// create a new HibernateProxy for each id.
					List<Object> proxies = new ArrayList<Object>();
					for (IHibernateProxyDescriptor proxyDescriptor : proxyDescriptors)
					{
						ASObject as = getASObject(proxyDescriptor);
						proxies.add(as);
					}
					return proxies;
				}
			}
		} catch (Throwable ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	private ASObject getASObject(IHibernateProxyDescriptor proxyDescriptor)
	{
		ASObject asObject = new ASObject();
		asObject.setType(proxyDescriptor.getRemoteClassName());
		asObject.put(HibernateProxyConstants.UID, UUID.randomUUID().toString());
		asObject.put(HibernateProxyConstants.PKEY, proxyDescriptor.getProxyId());
		asObject.put(HibernateProxyConstants.PROXYINITIALIZED, false);
		return asObject;
	}
}
