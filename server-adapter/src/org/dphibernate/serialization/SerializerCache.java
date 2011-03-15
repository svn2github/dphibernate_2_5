package org.dphibernate.serialization;

import java.util.HashMap;


import org.dphibernate.core.IEntity;
import org.dphibernate.persistence.state.CollectionChangeMessage;
import org.dphibernate.persistence.state.ObjectChangeMessage;
import org.dphibernate.persistence.state.PropertyChangeMessage;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.proxy.HibernateProxy;

/**
 * A Cache of serialized objects.
 * Intended to be short-lived, normally a chache is spawned per 
 * root object being serialized.
 * @author Marty Pitt
 *
 */
public class SerializerCache {

	private HashMap<Object,Object> cache = new HashMap<Object,Object>();
	public Object getCacheKey(HibernateProxy obj) {
		String className = obj.getHibernateLazyInitializer().getPersistentClass().getName().toString();
		String objectId = obj.getHibernateLazyInitializer().getIdentifier().toString(); 
		return className + "_" + objectId;
	}

	public Object getCacheKey(AbstractPersistentCollection obj) {
		if (obj.wasInitialized() && obj.getRole() != null && obj.getKey() != null) {
			return obj.getRole() + "_" + obj.getKey().toString();
		} else {
			return null;
		}
	}

	public Object getCacheKey(IEntity obj) {
		return new EntityCacheKey((IEntity) obj);
	}

	public Object getCacheKey(Object obj) {
		if (obj instanceof HibernateProxy) {
			return getCacheKey((HibernateProxy) obj);
		} else if (obj instanceof AbstractPersistentCollection)
		{
			return getCacheKey((AbstractPersistentCollection) obj);
		} else {
			return obj;
		}
	}

	public void store(Object key, Object value) {
		if(TypeHelper.isSimple(value)) return;
		if(key == null) return;
		cache.put(key, value);
	}
	public Object get(Object key)
	{
		return cache.get(key);
	}
	public void invalidate(Object key)
	{
		cache.remove(key);
	}
	public void invalidate(ObjectChangeMessage objectChangeMessage, Object entity)
	{
		String className = objectChangeMessage.getOwner().getRemoteClassName();
		String objectId = objectChangeMessage.getOwner().getProxyId().toString();
		String objectKey = className + "_" + objectId;
		invalidate(objectKey);
		for(PropertyChangeMessage propertyChangeMessage : objectChangeMessage.getChangedProperties())
		{
			if(propertyChangeMessage instanceof CollectionChangeMessage)
			{
				String role = className + "." + propertyChangeMessage.getPropertyName();
				String collectionKey = role + "_" + objectId;
				invalidate(collectionKey);
			}
		}
		
	}
	public boolean contains(Object key)
	{
		return cache.containsKey(key);
	}
}
