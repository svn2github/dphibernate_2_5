package org.dphibernate.rpc
{
	import org.dphibernate.core.IEntity;

	/**
	 * Defines a provider that maps entities to a HibernateRemoteObject
	 * which can be used to populate proxies.*/
	public interface IRemoteObjectProvider
	{
		function getRemoteObject(bean:IEntity):IEntityService;
	}
}