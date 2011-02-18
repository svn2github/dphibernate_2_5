package org.dphibernate.serialization;

import java.util.Date;

import org.dphibernate.core.IHibernateProxy;



public class EntityCacheKey {
	private final Object id;
	private final Class entityClass;
	private IHibernateProxy entity;
	public EntityCacheKey(IHibernateProxy entity)
	{
		this.entity = entity;
		this.entityClass = entity.getClass();
		if (entity.getProxyKey() == null)
		{
			this.id = new Date().getTime();
		} else {
			this.id = entity.getProxyKey();
		}
	}
	@Override
	public boolean equals(Object val)
	{
		if (val instanceof EntityCacheKey)
		{
			EntityCacheKey cacheKeyB = (EntityCacheKey) val;
			return entity.equals(cacheKeyB.entity);
		}
		else
		{
			return false;
		}
	}
	@Override
	public int hashCode()
	{
		return entity.hashCode();
	}
	
	
}
