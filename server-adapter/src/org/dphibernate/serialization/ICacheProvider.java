package org.dphibernate.serialization;

import java.beans.IntrospectionException;

public interface ICacheProvider {
	public void examine(Object object) throws IntrospectionException;

	public Object getCacheKey(Object obj);

	public boolean contains(Object key);

	public Object get(Object key);

	public void put(Object key, Object obj);
	
	public CacheProviderState getCacheState(Object object);
	
	public boolean hasPendingValues();
}
