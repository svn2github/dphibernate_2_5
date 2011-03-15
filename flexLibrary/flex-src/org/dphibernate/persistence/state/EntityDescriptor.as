package org.dphibernate.persistence.state
{
	import org.dphibernate.core.IEntity;
	import org.dphibernate.util.ClassUtils;
	
	[RemoteClass(alias="org.dphibernate.persistence.state.HibernateProxyDescriptor")]	
	public class EntityDescriptor implements IEntityDescriptor
	{
		public function EntityDescriptor( source : IEntity = null )
		{
			_remoteClassName = ClassUtils.getRemoteClassName( source );
			this.source = source;
		}
		private var _remoteClassName : String;
		public function get remoteClassName():String
		{
			return _remoteClassName;
		}
		public function set remoteClassName( value : String ) : void
		{
			_remoteClassName = value;
		}
		private var _proxyId:Object;
		public function get proxyId():Object
		{
			return _proxyId;
		}
		public function set proxyId( value : Object ) : void
		{
			_proxyId = value;
		}
		private var _source : IEntity;
		[Transient]
		public function get source() : IEntity
		{
			return _source;
		}
		public function set source(value:IEntity):void
		{
			_source = value;
			if (source)
				_proxyId = source.entityKey;
		}
	}
}