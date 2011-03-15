package org.dphibernate.entitymanager
{
	
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.IList;
	import mx.utils.StringUtil;
	
	import org.dphibernate.core.IEntity;

	/**
	 * Resolves entities based on the concept of Identity.
	 * Eg., an entity has an identity (ie., it's primary key).
	 * We receive many instances of this entity from the server via remoting calls.
	 * The EntityResolver will take two instances of the same entity, and return a common
	 * entity.
	 **/
	public class EntityManager implements IEntityManager
	{
		private var entities:Object = new Object(); // Map of Key (from getKey()) and Entity
		private var defaultResolutionPolicy:IResolutionPolicy;
		public function EntityManager(defaultResolutionPolicy:IResolutionPolicy=null)
		{
			this.defaultResolutionPolicy = defaultResolutionPolicy ||= new NewestIsBestResolutionPolicy();
		}
		
		/**
		 * Resolves the entity, ensuring that only one version is present throughout the system.
		 * If this is the first time we have seen the entity, it is stored.
		 * */
		public function resolve(entity:IEntity,resolutionPolicy:IResolutionPolicy=null):*
		{
			resolutionPolicy ||= defaultResolutionPolicy;
			if (!contains(entity))
			{
				store(entity)
			}
			var oldVersion:IEntity = findMatchingEntity(entity) as IEntity;
			return resolutionPolicy.getResolvedEntity(oldVersion,entity,this);
		}
		
		/**
		 * Resolves a collection of entities.
		 * */
		public function resolveCollection(collection:IList,resolutionPolicy:IResolutionPolicy=null):*
		{
			for (var i:int = 0; i < collection.length; i++)
			{
				var resolvedItem:* = resolve(collection.getItemAt(i) as IEntity,resolutionPolicy);
				collection.setItemAt(resolvedItem,i);
			}
		}
		/**
		 * Resolves an array of entities.
		 * */
		public function resolveArray(array:Array,resolutionPolicy:IResolutionPolicy=null):*
		{
			for (var i:int = 0; i < array.length; i++)
			{
				var originalItem:* = array[i];
				var resolvedItem:* = resolve(originalItem as IEntity,resolutionPolicy);
				array[i] = resolvedItem;
			}
		}

		public function findMatchingEntity(entity:IEntity):*
		{
			return retrieve(getKey(entity));
		}
		private function retrieve(key:String):*
		{
			return entities[key];
		}

		internal function store(entity:IEntity):void
		{
			var key:String = getKey(entity);
			entities[key] = entity;
		}
		
		
		public function contains(entity:IEntity):Boolean
		{
			return containByKey(getKey(entity)); 
		}

		private function containByKey(key:String):Boolean
		{
			return entities[key] != null;
		}
		private function getKey(entity:IEntity):String
		{
			return StringUtil.substitute("{0}.{1}",getQualifiedClassName(entity),entity.entityKey);
		}
	}
}