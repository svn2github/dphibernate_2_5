package org.dphibernate.loader.service
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	public dynamic class RemotingLazyLoadService extends RemoteObject implements ILazyLoadService
	{
		public function RemotingLazyLoadService(destination:String=null)
		{
			super(destination);
		}

		public function loadProxy(entityKey:Object, remoteClassName:String):AsyncToken
		{
			return this.loadDPProxy(entityKey,remoteClassName);
		}
	}
}