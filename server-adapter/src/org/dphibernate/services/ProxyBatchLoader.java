package org.dphibernate.services;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dphibernate.core.DPHibernateException;
import org.dphibernate.core.IEntity;
import org.dphibernate.utils.ProxyUtil;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.ArrayListMultimap;

public class ProxyBatchLoader implements IProxyBatchLoader
{

	SessionFactory sessionFactory;


	public ProxyBatchLoader(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}


	@Override
	public List<ProxyLoadResult> loadProxyBatch(ProxyLoadRequest[] requests)
	{
		List<ProxyLoadResult> results = new ArrayList<ProxyLoadResult>();
		Map<String, Collection<Serializable>> requestsByClass = getRequestsByClass(requests);
		Set<Entry<String, Collection<Serializable>>> requestClassEntrySet = requestsByClass.entrySet();
		for (Entry<String, Collection<Serializable>> requestClassEntry : requestClassEntrySet)
		{
			List<Object> loadedEntities = loadEntities(requestClassEntry);
			List<ProxyLoadResult> mappedResults = mapLoadedEntitesToOriginalRequest(loadedEntities, requests);
			results.addAll(mappedResults);
		}
		return results;
	}


	List<ProxyLoadResult> mapLoadedEntitesToOriginalRequest(List<? extends Object> loadedEntities, ProxyLoadRequest[] requests)
	{
		List<ProxyLoadResult> result = new ArrayList<ProxyLoadResult>();
		for (Object entity : loadedEntities)
		{
			assertEntityIsHibernateProxy(entity);
			IEntity proxy = (IEntity) entity;
			ProxyLoadRequest matchingRequest = findProxyLoadRequestForProxy(proxy, requests);
			assertResultWasInListOfRequests(entity, matchingRequest);
			ProxyLoadResult proxyLoadResult = new ProxyLoadResult(matchingRequest.getRequestKey(), entity);
			result.add(proxyLoadResult);
		}
		return result;
	}


	private void assertResultWasInListOfRequests(Object entity, ProxyLoadRequest matchingRequest)
	{
		if (matchingRequest == null)
		{
			throw new RuntimeException("An entity was returned from the database which was not requested: " + entity.toString());
		}
	}


	private ProxyLoadRequest findProxyLoadRequestForProxy(IEntity proxy, ProxyLoadRequest[] requests)
	{
		for (ProxyLoadRequest request:requests)
		{
			if (request.matchesEntity(proxy))
			{
				return request;
			}
		}
		return null;
	}


	private void assertEntityIsHibernateProxy(Object en)
	{
		if (!(en instanceof IEntity))
		{
			throw new RuntimeException("Returned object is not IHibernateProxy.  Bulk loading not supported");
		}
	}


	@SuppressWarnings("unchecked")
	private List<Object> loadEntities(Entry<String, Collection<Serializable>> requestClassEntry)
	{
		Class<?> requestClass = getRequestClass(requestClassEntry.getKey());
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(requestClass);
		criteria.add(Restrictions.in("id", requestClassEntry.getValue()));
		List<Object> results = criteria.list();
		return results;
		// session.get
		// String keyName;
		// IdentifierEqExpression identifierExpression =
		// IdentifierEqExpression();
		// Restrictions.naturalId().
		// criteria.add(Restrictions.in(keyName, requestClassEntry.getValue()));
	}


	private Class<?> getRequestClass(String className)
	{
		try
		{
			Class<?> entryClass = Class.forName(className);
			return entryClass;
		} catch (ClassNotFoundException e)
		{
			throw new DPHibernateException(className + " is not a recognized class");
		}
	}


	Map<String, Collection<Serializable>> getRequestsByClass(ProxyLoadRequest[] requests)
	{
		ArrayListMultimap<String, Serializable> requestsByClass = ArrayListMultimap.create();
		for (ProxyLoadRequest request : requests)
		{
			// Convert proxyID in the ProxyLoadRequest to the type defined in the entity object.
			Serializable proxyID = ProxyUtil.convertProxyIdInProxyLoadRequestToEntityIdType(request, sessionFactory);			
			requestsByClass.put(request.getClassName(), proxyID);
		}
		return requestsByClass.asMap();
	}
	

}
