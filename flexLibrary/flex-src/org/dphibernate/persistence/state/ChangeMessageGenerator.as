package org.dphibernate.persistence.state
{
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.dphibernate.util.ClassUtils;
	import org.dphibernate.core.IHibernateProxy;

	public class ChangeMessageGenerator
	{
		private var objectChangeMessageCollection : ObjectChangeMessageCollection;
		private var recursionTracker : ArrayCollection;
		
		public function ChangeMessageGenerator()
		{
			objectChangeMessageCollection = new ObjectChangeMessageCollection();
			recursionTracker = new ArrayCollection();
		}
		public function getChangesForEntityOnly(object:IHibernateProxy):ObjectChangeMessage
		{
			var result:Array=getChanges(object, false, true);
			if (result.length == 0)
				return null;
			return (result[0] as ObjectChangeMessage);
		}
		public function hasChanges( object : IHibernateProxy ) : Boolean
		{
			var changes : Array = getChanges( object );
			for each ( var changeMessage : ObjectChangeMessage in changes )
			{
				if ( changeMessage.numChanges > 0 ) return true;
			}
			return false;
		}
		public function getChanges(object:IHibernateProxy, cascade:Boolean=true, includeUnchangedObject:Boolean=false):Array // Of ObjectChangeMessage
		{
			var changeCollection:ObjectChangeMessageCollection=doGetChanges(object, cascade, includeUnchangedObject);
			return cloneObjectChangeMessages(changeCollection);
		}
		private function cloneObjectChangeMessages(changeMessageCollection:ObjectChangeMessageCollection):Array
		{
			var result:Array =[];
			for each (var objectChangeMessage:ObjectChangeMessage in changeMessageCollection.changeMessages)
			{
				result.push(objectChangeMessage.clone());
			}
			return result;
		}


		// Recursion entry point
		private function doGetChanges(object:IHibernateProxy, cascade:Boolean=true, includeUnchangedObject:Boolean=false):ObjectChangeMessageCollection
		{
			if (ClassUtils.isImmutable( object ) )
			{
				return objectChangeMessageCollection;
			}
			var key:String=StateRepository.getKey(object);
			if (StateRepository.isNew(object) && !StateRepository.contains(object))
			{
				StateRepository.addNewObject(object);
			}
			var objectChangeMessage:ObjectChangeMessage;
			objectChangeMessage=StateRepository.getStoredChanges(object);


			if (objectChangeMessage && objectChangeMessage.numChanges > 0 || (objectChangeMessage && objectChangeMessage.numChanges == 0 && includeUnchangedObject))
			{
				objectChangeMessageCollection.add(objectChangeMessage);
			}
			if (!cascade)
				return objectChangeMessageCollection;

			if (recursionTracker.contains(key))
				return objectChangeMessageCollection;
			recursionTracker.addItem(key);

			appendChangesOfChildren(object);

			return objectChangeMessageCollection;
		}

		internal function appendChangesOfChildren(object:IHibernateProxy):void
		{
			var childrenValue:ArrayCollection=StateRepository.getChildrenValues(object);
			appendChangesForList(childrenValue);
		}

		internal function appendChangesForList(list:IList):void
		{
			for each (var child:Object in list)
			{
				if (child is IHibernateProxy)
				{
					doGetChanges(child as IHibernateProxy, true, false);
				}
				if (child is IList)
				{
					appendChangesForList(child as IList);
				}
			}
		}

	}
}