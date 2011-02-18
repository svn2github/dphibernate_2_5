package org.dphibernate.persistence.state
{
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.util.ClassUtils;
	
	[RemoteClass(alias="org.dphibernate.persistence.state.HibernateProxyDescriptor")]	
	public class HibernateProxyDescriptor implements IHibernateProxyDescriptor
	{
		public function HibernateProxyDescriptor( source : IHibernateProxy = null )
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
		private var _source : IHibernateProxy;
		[Transient]
		public function get source() : IHibernateProxy
		{
			return _source;
		}
		public function set source(value:IHibernateProxy):void
		{
			_source = value;
			if (source)
				_proxyId = source.proxyKey;
		}
	}
}