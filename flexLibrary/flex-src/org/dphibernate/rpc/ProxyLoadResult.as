package org.dphibernate.rpc
{
	[RemoteClass(alias="org.dphibernate.services.ProxyLoadResult")]
	public class ProxyLoadResult
	{
		public var requestKey:String;
		public var result:Object;
	}
}