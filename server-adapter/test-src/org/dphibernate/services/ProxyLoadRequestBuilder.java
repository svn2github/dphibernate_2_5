package org.dphibernate.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dphibernate.services.ProxyLoadRequest;


public class ProxyLoadRequestBuilder
{

	private final Class<?> entityClass;
	public ProxyLoadRequestBuilder(Class<?> entityClass){
		this.entityClass = entityClass;
		
	}
	public static ProxyLoadRequestBuilder forEntity(Class<?> entityClass)
	{
		return new ProxyLoadRequestBuilder(entityClass);
	}
	public ProxyLoadRequest[] withIds(Serializable... ids)
	{
		List<ProxyLoadRequest> requests = new ArrayList<ProxyLoadRequest>();
		for (Serializable id:ids)
		{
			requests.add(new ProxyLoadRequest(entityClass.getCanonicalName(), id));
		}
		return requests.toArray(new ProxyLoadRequest[requests.size() - 1]);
	}

}
