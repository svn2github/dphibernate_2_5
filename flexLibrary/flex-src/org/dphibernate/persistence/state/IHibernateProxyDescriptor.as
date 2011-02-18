package org.dphibernate.persistence.state
{
	import org.dphibernate.core.IHibernateProxy;
	
	public interface IHibernateProxyDescriptor
	{
		function get remoteClassName() : String;
		function get proxyId() : Object;
		function get source() : IHibernateProxy;
	}
}