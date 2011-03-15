package org.dphibernate.core
{
	import flash.events.EventDispatcher;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	import org.dphibernate.model.Book;
	
	public class BaseEntity extends EventDispatcher implements IUpdatableEntity
	{
		private var _entityKey:Object;
		private var _entityInitialized:Boolean;
		private var _entityStatus:int;
		public function BaseEntity()
		{
		}
		
		public function get entityInitialized():Boolean
		{
			return _entityInitialized;
		}
		
		public function set entityInitialized(value:Boolean):void
		{
			_entityInitialized = value;
		}
		
		public function get entityKey():Object
		{
			return _entityKey;
		}
		
		public function set entityKey(value:Object):void
		{
			_entityKey = value;
		}
		
		public function save(responder:IResponder=null):AsyncToken
		{
			return null;
		}
		
		public function deleteRecord(responder:IResponder=null):AsyncToken
		{
			return null;
		}

		public function get entityStatus():int
		{
			return _entityStatus;
		}

		public function set entityStatus(value:int):void
		{
			_entityStatus = value;
		}
	}
}