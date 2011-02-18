package org.dphibernate.utils;

import java.io.Serializable;

import org.dphibernate.core.DPHibernateException;
import org.dphibernate.services.ProxyLoadRequest;
import org.hibernate.SessionFactory;

public class ProxyUtil {
	
	/**
	 * Converts the proxyID to the Id type defined in the Entity class.
	 */
	public static Serializable convertProxyIdToEntityIdType(Serializable proxyID, Class<?> entityClass, SessionFactory sessionFactory)
	{
		// Get the entity Id class by instrospection (must have Id.class in annotations)
		Class<?> entityIdClass = EntityUtil.getEntityIdClass(entityClass, sessionFactory);
		
		// If the entityIdClass is found, convert the id to the correct type
		if(entityIdClass != null && proxyID instanceof Number)
		{	
			proxyID = NumberUtil.convertSerializableToNumberType(proxyID, entityIdClass);
		}
		return proxyID;
	}
	

	/**
	 * Converts the proxyID in the ProxyLoadRequest instance to the Id type defined in the Entity class.
	 */
	public static Serializable convertProxyIdInProxyLoadRequestToEntityIdType(ProxyLoadRequest proxyLoadRequest, SessionFactory sessionFactory){
		Class<?> entityClass = null;
		String entityClassName = proxyLoadRequest.getClassName();
		entityClass = getClassByName(entityClassName);
		
		Serializable proxyID = proxyLoadRequest.getProxyID();
		proxyID = ProxyUtil.convertProxyIdToEntityIdType(proxyID, entityClass, sessionFactory);

		// Set the proxyID in the ProxyLoadRequest instance for match comparisons.
		proxyLoadRequest.setProxyID(proxyID);
		
		return proxyLoadRequest.getProxyID();		
	}
	
	private static Class<?> getClassByName(String className)
	{
		try
		{
			Class<?> theClass = Class.forName(className);
			return theClass;
		} catch (ClassNotFoundException e)
		{
			throw new DPHibernateException(className + " is not a recognized class");
		}
	}
}
