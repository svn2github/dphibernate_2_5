package org.dphibernate.context
{
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.loader.service.ILazyLoadServiceProvider;

	public interface Context
	{
		function get entityManager():IEntityManager;
		function get lazyLoaderServiceProvider():ILazyLoadServiceProvider;
		function createEntity( clazz:Class, args:Array = null ):* 
	}
}