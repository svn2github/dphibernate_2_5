package org.dphibernate.services;

import java.io.Serializable;
import java.util.Map;

import org.dphibernate.serialization.LegacyPropertyHelper;
import org.dphibernate.utils.ProxyUtil;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyLoadService implements IProxyLoadService
{
	
	private Logger log = LoggerFactory.getLogger(ProxyLoadService.class);
	private final SessionFactory sessionFactory;
	
	public ProxyLoadService(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Object loadBean(Class daoClass, Serializable id)
	{
		if (id==null)
		{
			throw new RuntimeException("Supplied ID is null");
		}
		log.info("loadByClass : " + daoClass.getCanonicalName() + "::" + id.toString());
		if (id instanceof String)
		{
			@SuppressWarnings("unused")// For debugging...
			String originalId = (String) id;
			id = Integer.parseInt((String) id);
		}
		
		// Convert id to the type defined in the entity object.
		Serializable proxyID = ProxyUtil.convertProxyIdToEntityIdType(id, daoClass, sessionFactory);
		
		Object result = sessionFactory.getCurrentSession().get(daoClass, proxyID);
		return result;
	}


	@Override
	public Map<String, Object> loadProperties(Class<?> daoClass, Serializable id)
	{
		Object bean = loadBean(daoClass, id);
		Map<String,Object> properties = LegacyPropertyHelper.getProperties(bean);
		return properties;
	}
}
