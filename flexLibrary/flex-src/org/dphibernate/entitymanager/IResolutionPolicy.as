package org.dphibernate.entitymanager
{
	import mx.core.IUID;
	
	import org.dphibernate.core.IEntity;

	public interface IResolutionPolicy
	{
		function getResolvedEntity(oldVersion:IEntity,newVersion:IEntity, entityResolver:EntityManager):*;
	}
}