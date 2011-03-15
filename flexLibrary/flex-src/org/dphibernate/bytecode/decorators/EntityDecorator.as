package org.dphibernate.bytecode.decorators
{
	import flash.utils.getQualifiedClassName;
	
	import org.as3commons.reflect.Field;
	import org.as3commons.reflect.Type;
	import org.dphibernate.core.IEntity;
	import org.dphibernate.core.Metadata;

	/**
	 * Bytecode delegate for IEntity introduction
	 * */
	public class EntityDecorator implements IEntity
	{
		private var initialized:Boolean = false;
		private var source:Object;
		private var type:Type
		private var idField:Field;
		private var status:int = 0;
		
		public function EntityDecorator(source:Object):void
		{
			this.source = source;
			this.type = Type.forInstance(source);
			initializeIdentityField();
		}

		private function initializeIdentityField():void
		{
			var containers:Array = type.getMetadataContainers(Metadata.ID);
			if (containers.length == 0)
			{
				throw new Error("No [Id] specified on " + getQualifiedClassName(source));
			}
			if (containers.length > 1)
			{
				throw new Error("Multiple [Id] specificed on " + getQualifiedClassName(source));
			}
			idField = containers[0];
		}
		public function get entityKey():Object
		{
			return idField.getValue(source);
		}

		public function set entityKey(value:Object):void
		{
			// TODO when I come to set up entity persistence.
			// Hmmm... therefore - should this be in the persistence decorator?
			throw new Error("Not Implemented");
		}

		public function get entityInitialized():Boolean
		{
			return initialized;
		}

		public function set entityInitialized(value:Boolean):void
		{
			initialized = value;
		}

		public function get entityStatus():int
		{
			return status;
		}

		public function set entityStatus(value:int):void
		{
			status = value;
		}
	}
}