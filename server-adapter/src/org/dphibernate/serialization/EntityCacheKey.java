package org.dphibernate.serialization;

import java.util.Date;

import org.dphibernate.core.IEntity;



public class EntityCacheKey {
	private final Object id;
	private final Class entityClass;
	private IEntity entity;
	public EntityCacheKey(IEntity entity)
	{
		this.entity = entity;
		this.entityClass = entity.getClass();
		if (entity.getEntityKey() == null)
		{
			this.id = new Date().getTime();
		} else {
			this.id = entity.getEntityKey();
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
