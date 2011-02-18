package org.dphibernate.rpc
{
	import org.dphibernate.core.IHibernateProxy;

	/**
	 * Defines a provider that maps entities to a HibernateRemoteObject
	 * which can be used to populate proxies.*/
	public interface IHibernateROProvider
	{
		function getRemoteObject(bean:IHibernateProxy):IHibernateRPC;
	}
}