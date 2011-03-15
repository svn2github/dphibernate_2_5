package org.dphibernate.persistence.state
{
	import org.dphibernate.core.IEntity;
	
	public interface IEntityDescriptor
	{
		function get remoteClassName() : String;
		function get proxyId() : Object;
		function get source() : IEntity;
	}
}