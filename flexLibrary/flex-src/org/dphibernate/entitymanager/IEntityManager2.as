package org.dphibernate.entitymanager
{
	import org.dphibernate.core.IEntity;
	import org.dphibernate.persistence.state.IEntityDescriptor;

	// TODO : Mp - I don't think we're gonna need this .. but I don't wann acan it just yet
	public interface IEntityManager2
	{
		function putEntity(proxy:IEntity):String;
		function containsEntity(proxy:IEntity):Boolean;
		function getEntity(key:IEntity):IEntity;
		function evictEntity(proxy:IEntity):IEntity; // Returns the evicted element
		
		function findForDescriptor(descriptor:IEntityDescriptor):IEntity;
		function containsForDescriptor(descriptor:IEntityDescriptor):Boolean;
		function findForKey(key:String):IEntity;
		function containsKey(key:String):Boolean;
	}
}