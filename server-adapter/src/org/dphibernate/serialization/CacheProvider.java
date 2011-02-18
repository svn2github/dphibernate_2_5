package org.dphibernate.serialization;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.Resource;


import org.dphibernate.core.IHibernateProxy;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.proxy.HibernateProxy;

public class CacheProvider implements ICacheProvider {

	private HashMap<Object, Object> cache = new HashMap<Object, Object>();
	private HashMap<Object, Object> pendingValues = new HashMap<Object, Object>();
	@Resource
	private ITranslationProvider translationProvider;

	public CacheProvider(ITranslationProvider translationProvider) {
		this.translationProvider = translationProvider;
	}

	
	private void examineCollection(Collection collection)
	{
		if (collection instanceof AbstractPersistentCollection)
		{
			// TODO : Handle lazy-loaded collections in the same way
			return;
		}
		for(Object collectionMember : collection)
		{
			examine(collectionMember);
		}
	}
	private void examineObject(Object object)
	{
		try {
			Object cacheKey = getCacheKey(object);
			if (translationProvider.willTranslate(object))
			{
				pendingValues.put(cacheKey, object);
			}
			BeanInfo info = Introspector.getBeanInfo(object.getClass());

			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String propName = pd.getName();
				if (!"handler".equals(propName) && !"class".equals(propName)
						&& pd.getReadMethod() != null) {
					Object val = pd.getReadMethod().invoke(object, null);
					if (val != null)
					{
						examine(val);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void examine(Object object) {
		if (!translationProvider.willTranslate(object)) {
			return;
		}
		if (getCacheState(object) != CacheProviderState.UNKNOWN)
		{
			return;
		}
		if (object instanceof Collection)
		{
			examineCollection((Collection) object);
		} else {
			examineObject(object);
		}
		
		
	}

	public Object getCacheKey(Object obj) {
		if (obj instanceof HibernateProxy) {
			return ((HibernateProxy) obj).getHibernateLazyInitializer()
					.getPersistentClass().getName().toString()
					+ "_"
					+ ((HibernateProxy) obj).getHibernateLazyInitializer()
							.getIdentifier().toString();
		} else if (obj instanceof AbstractPersistentCollection
				&& !((AbstractPersistentCollection) obj).wasInitialized()) {
			return ((AbstractPersistentCollection) obj).getRole() + "_"
					+ ((AbstractPersistentCollection) obj).getKey().hashCode();
		} else if (obj instanceof IHibernateProxy) {
			return new EntityCacheKey((IHibernateProxy) obj);
		}
		return obj;
	}

	public boolean contains(Object key) {
		// TODO Auto-generated method stub
		return cache.containsKey(key);
	}

	public Object get(Object key) {
		// TODO Auto-generated method stub
		return cache.get(key);
	}

	public void put(Object key, Object value) {
		cache.put(key, value);
		if (value instanceof Collection)
		{
			for( Object collectionMember : (Collection) value)
			{
				cache.put(getCacheKey(collectionMember), collectionMember);
			}
		}
	}

	public CacheProviderState getCacheState(Object object) {
		Object cacheKey = getCacheKey(object);
		if (cache.containsKey(cacheKey))
		{
			return CacheProviderState.CACHED;
		}
		if (pendingValues.containsKey(cacheKey))
		{
			return CacheProviderState.PENDING;
		}
		return CacheProviderState.UNKNOWN;
	}


	public boolean hasPendingValues() {
		return pendingValues.size() > 0;
	}

}
