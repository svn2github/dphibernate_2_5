package org.dphibernate.rpc
{
	import mx.rpc.AsyncToken;
	import mx.utils.UIDUtil;

	[RemoteClass(alias="org.dphibernate.services.ProxyLoadRequest")]
	public class ProxyLoadRequest
	{
		private var _internalAsyncToken:AsyncToken;
		private var _className:String;
		private var _proxyID:Object;
		private var _requestKey:String;
		
		public function ProxyLoadRequest(proxyID:Object=null, remoteClassName:String=null, internalAsyncToken:AsyncToken=null)
		{
			this._className=remoteClassName;
			this._proxyID=proxyID;
			this._internalAsyncToken=internalAsyncToken;
			this._requestKey = UIDUtil.createUID();
		}

		[Transient]
		public function get internalAsyncToken():AsyncToken
		{
			return _internalAsyncToken;
		}

		public function get className():String
		{
			return _className;
		}
		

		public function set internalAsyncToken(value:AsyncToken):void
		{
			_internalAsyncToken = value;
		}

		public function set className(value:String):void
		{
			_className = value;
		}

		public function set proxyID(value:Object):void
		{
			_proxyID = value;
		}

		public function set requestKey(value:String):void
		{
			_requestKey = value;
		}


		public function get proxyID():Object
		{
			return _proxyID;
		}
		public function get requestKey():String
		{
			return _requestKey;
		}

	}
}