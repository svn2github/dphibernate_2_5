package org.dphibernate.persistence.state
{
	import mx.collections.IList;
	
	import org.dphibernate.core.IHibernateProxy;
	[RemoteClass(alias="org.dphibernate.persistence.state.CollectionChangeMessage")]	
	public class CollectionChangeMessage extends PropertyChangeMessage
	{
		private var _collection : IList
		public function CollectionChangeMessage( propertyName : String , collection : IList , generator  : ChangeMessageGenerator = null )
		{
			super( propertyName , null , null );
			_collection = collection;
			_generator = ( generator ) ? generator :  new ChangeMessageGenerator();
		}
		
		public override function get newValue() : Object // Array of ObjectChangeMessage
		{
			var result : Array = []
			for each ( var proxy : IHibernateProxy in _collection )
			{
				var changes : ObjectChangeMessage = generator.getChangesForEntityOnly( proxy );
				if ( changes && changes.numChanges > 0 )
				{
					result.push( changes );
				} else {
					result.push( generateNoChangeMessage( proxy ) );
				}
				/*
				if ( StateRepository.hasChanges( proxy  ) )
				{
					result.push( ChangeMessageGenerator.getChangesForEntityOnly( proxy ) )
				} else {
					result.push( generateNoChangeMessage( proxy ) );
				}
				*/
			}
			return result;
		}
		override public function get oldAndNewValueMatch() : Boolean
		{
			return false;
		}
		private function generateNoChangeMessage( proxy : IHibernateProxy ) : ObjectChangeMessage
		{
			var message : ObjectChangeMessage = new ObjectChangeMessage( new HibernateProxyDescriptor( proxy ) );
			return message;
		}
		private var _generator : ChangeMessageGenerator;
		public function get generator() : ChangeMessageGenerator
		{
			return _generator;
		}
		public function set generator( value : ChangeMessageGenerator ) : void
		{
			_generator = value;
		}
		override public function merge(valueToMerge:PropertyChangeMessage):PropertyChangeMessage
		{
			var mergeCollection:CollectionChangeMessage = CollectionChangeMessage(valueToMerge);
			return mergeCollection;
		}
	}
}