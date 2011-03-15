package org.dphibernate.persistence.state
{
	import mx.rpc.remoting.RemoteObject;
	
	import org.dphibernate.core.IEntity;
	import org.dphibernate.rpc.IEntityService;

	public class UpdateMetadata
	{
		private var _entity:IEntity;
		private var _changes:Array;

		private var _serivce:IEntityService;		

		public function UpdateMetadata(service:IEntityService, entity:IEntity, changes:Array)
		{
			_serivce = service;
			_entity = entity;  
			_changes = changes;  
			super();
		}

		public function get serivce():IEntityService
		{
			return _serivce;
		}

		public function get entity():IEntity
		{
			return _entity;
		}

		public function get changes():Array
		{
			return _changes;
		}

	}
}