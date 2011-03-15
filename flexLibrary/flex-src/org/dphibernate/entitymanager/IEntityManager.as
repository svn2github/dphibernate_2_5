package org.dphibernate.entitymanager
{
	import mx.collections.IList;
	
	import org.dphibernate.core.IEntity;

	public interface IEntityManager
	{
				function resolve(entity:IEntity,resolutionPolicy:IResolutionPolicy=null):*;

		function resolveCollection(collection:IList,resolutionPolicy:IResolutionPolicy=null):*;

		function resolveArray(array:Array,resolutionPolicy:IResolutionPolicy=null):*;

		function findMatchingEntity(entity:IEntity):*;

		function contains(entity:IEntity):Boolean;

	}
}