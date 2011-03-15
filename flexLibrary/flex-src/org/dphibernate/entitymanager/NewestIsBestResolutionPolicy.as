package org.dphibernate.entitymanager
{
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.dphibernate.core.IEntity;
	import org.spicefactory.lib.reflect.ClassInfo;
	import org.spicefactory.lib.reflect.Property;

	/**
	 * A simple resolution policy, which merges data from the "newVersion" over the "oldVersion".
	 * 
	 * The theory is that if the newVersion has come from the server, it contains better
	 * knowledge than what the oldVersion contains.
	 * 
	 * The data from newVersion is updated onto oldVersion (with some exceptions)
	 * and the oldVersion is returned.
	 * 
	 * An exception to this rule is nulls.
	 * 
	 * When a null is received from the server on a newVersion, and the oldVersion
	 * contains a value, it is possible that newVersion is not a fully populated
	 * object graph.r
	 * 
	 * Therefore, whether or not this value is copied from new to old is determined by
	 * copyNullValuesFromNewToOld.
	 * 
	 * If copyNullValuesFromNewToOld is true, then the oldVersion will have it's property
	 * updated with null. Otherwise (default) it is left alone.
	 * 
	 * Note - we cannot return the newVersion, as other instances of oldVersion
	 * are in use throughout the system. Instead, keep oldVersion as the autoritative
	 * entity, but just update it with data from newVersion.
	 * */
	internal class NewestIsBestResolutionPolicy implements IResolutionPolicy
	{
		public var copyNullValuesFromNewToOld:Boolean = false;
		
		public function getResolvedEntity(oldVersion:IEntity, newVersion:IEntity, entityResolver:EntityManager):*
		{
			var properties:Array = getProperties(oldVersion);
			for each (var property:Property in properties)
			{
				mergeProperty(property,oldVersion,newVersion,entityResolver);
			}
			return oldVersion;
		}

		/**
		 * 
		 * @param entity
		 * @return 
		 * 
		 */
		internal function getProperties(entity:IEntity):Array
		{
			var properties:Array = [];
			
			for each (var property:Property in ClassInfo.forInstance(entity).getProperties())
			{
				if (!property.readable || !property.writable)
					continue;
				
				if (property.hasMetadata("Transient"))
					continue;
				
				properties.push(property);
			}
			return properties;
		}

		private function mergeProperty(property:Property, oldVersion:IEntity, newVersion:IEntity, entityResolver:EntityManager):void
		{
			var newValue:* = property.getValue(newVersion);
			var oldValue:* = property.getValue(oldVersion);
			
			if (!hasValue(newValue) && !hasValue(oldValue))
				return; // Nothing to do.
			
			var applyUpdate:Boolean = true;
			if (!hasValue(newValue) && hasValue(oldValue))
			{
				if (!copyNullValuesFromNewToOld)
				{
					applyUpdate = false;
				}
			}
			if (isCollection(property.type) && applyUpdate)
			{
				applyUpdate = resolveCollection(oldValue,newValue,entityResolver);
			}
			if (isArray(property.type) && applyUpdate)
			{
				applyUpdate = resolveArray(oldValue,newValue,entityResolver);
			}
			if (isEntity(property.type) && applyUpdate)
			{
				// Note - pass null here (the default) so Mockito tests passs
				newValue = entityResolver.resolve(newValue as IEntity,null);
			}
			
			if (applyUpdate)
				oldVersion[property.name] = newValue;
		}

		internal function isArray(type:ClassInfo):Boolean
		{
			return type.getClass() == Array;
		}

		/** Updates the list.
		 * Returns a boolean to indicate if the caller still needs to apply the update, or if
		 * we have already handled it.
		 * 
		 * Generally, updating collections is handled within this method, where
		 * the contents of the new collection are copied to the old collection (replacing
		 * what was there -- note - entities are resolved appropriately.
		 * 
		 * The exception is if newList is populated, and oldList is not,
		 * in which case the caller should apply the update, simply setting newList
		 * as the value.
		 **/
		private function resolveCollection(oldList:IList,newList:IList,entityResolver:EntityManager):Boolean
		{
			// Quick exit -- does new list have a value where old list didn't?
			if (!oldList && newList)
				return true;
			
			// Note : don't use for..each here, as ArrayList doesn't support it
			for (var i:int = 0; i < newList.length; i++)
			{
				var newValue:* = newList.getItemAt(i);
				if (newValue && isEntity(ClassInfo.forInstance(newValue)))
				{
					newValue = entityResolver.resolve(newValue as IEntity,null);
				}
				if (oldList.length > i)
				{
					oldList.setItemAt(newValue,i);
				} else {
					oldList.addItemAt(newValue,i);
				}
			}
			
			return false;
		}
		private function resolveArray(oldArray:Array,newArray:Array,entityResolver:EntityManager):Boolean
		{
			// quick exit:  does newArray have a value, where oldArray didn't?
			if (!oldArray && newArray)
				return true;
			// Cheat .. wrap the arrays in collections so we can defer to resolveCollection
			// rather than duplicating logic
			return resolveCollection(new ArrayList(oldArray),new ArrayList(newArray),entityResolver);
		}

		internal function isCollection(type:ClassInfo):Boolean
		{
			return type.getInterfaces().indexOf(IList) != -1;
		}

		private function isEntity(type:ClassInfo):Boolean
		{
			return type.getInterfaces().indexOf(IEntity) != -1;
		}

		private function hasValue(value:*):Boolean
		{
			// TODO : Stub impl.
			// Need to consider type,etc. 
			// Also, "" should be treated as a value, not-null
			if (!value)
				return false;
			return true;
		}
	}
}