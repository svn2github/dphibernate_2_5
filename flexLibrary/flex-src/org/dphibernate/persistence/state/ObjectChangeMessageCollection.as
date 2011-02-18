package org.dphibernate.persistence.state
{
	import flash.utils.Dictionary;
	
	public class ObjectChangeMessageCollection
	{
		private var changes : Object = new Object() // key : Key , value : ObjectChangeMessage
		public function ObjectChangeMessageCollection()
		{
		}
		
		public function add( objectChangeMessage : ObjectChangeMessage ) : void 
		{
			var key : String = getKey( objectChangeMessage );
			if ( !containsKey( key ) )
			{
				changes[key] = objectChangeMessage;
			}
		}
		
		public function addAll( messages : Array ) : void
		{
			for each ( var message : ObjectChangeMessage in messages )
			{
				add( message );
			}
		}
		public function addCollection( collection : ObjectChangeMessageCollection ) : void
		{
			for each ( var message : ObjectChangeMessage in collection.changes )
			{
				add( message );
			}
		}
		public function contains(item:ObjectChangeMessage):Boolean
		{
			return containsKey( getKey( item ) );
		}
		private function containsKey( key : String ) : Boolean
		{
			return changes[key] != null;
		}
		
		private function getKey( message : ObjectChangeMessage ) : String
		{
			return message.owner.remoteClassName + "::" + message.owner.proxyId;
		}
		public function get changeMessages() : Array
		{
			var result : Array = []
			for each ( var object : Object in changes )
			{
				if ( object is ObjectChangeMessage ) result.push( object )
			}
			return result;
		}
	}
}