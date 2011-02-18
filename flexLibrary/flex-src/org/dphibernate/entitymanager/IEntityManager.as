package org.dphibernate.entitymanager
{
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.persistence.state.IHibernateProxyDescriptor;

	public interface IEntityManager
	{
		function putEntity(proxy:IHibernateProxy):String;
		function containsEntity(proxy:IHibernateProxy):Boolean;
		function getEntity(key:IHibernateProxy):IHibernateProxy;
		function evictEntity(proxy:IHibernateProxy):IHibernateProxy; // Returns the evicted element
		
		function findForDescriptor(descriptor:IHibernateProxyDescriptor):IHibernateProxy;
		function containsForDescriptor(descriptor:IHibernateProxyDescriptor):Boolean;
		function findForKey(key:String):IHibernateProxy;
		function containsKey(key:String):Boolean;
	}
}