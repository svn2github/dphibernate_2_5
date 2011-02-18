package org.dphibernate.persistence.state
{
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.data.IManaged;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.logging.ILogger;
	import mx.utils.UIDUtil;
	
	import org.dphibernate.collections.ManagedArrayList;
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.events.LazyLoadEvent;
	import org.dphibernate.rpc.HibernateManaged;
	import org.dphibernate.rpc.IHibernateRPC;
	import org.dphibernate.util.ClassUtils;
	import org.dphibernate.util.LogUtil;


	public class StateRepository
	{
		private static var log:ILogger=LogUtil.getLogger(StateRepository);
		private static var changeEntries:Object=new Object(); // Hash of Key : getKey() Value : ObjectChangeMessage
		private static var listTable:Dictionary=new Dictionary(true) // of Key: IList , value : PropertyReference
		private static var lazyLoadingEntities:Dictionary=new Dictionary(true);
		private static var newEntities:Object=new Object();
		private static var keyToUIDMap:Object = new Object();

		public static var ignorePropertyChanges:Boolean=false;

		public function StateRepository()
		{
		}

		public static function storeList(list:IList, owner:IHibernateProxy=null, propertyName:String=null):void
		{
			if (owner != null)
			{
				if (ClassUtils.isPropertyImmutable(owner, propertyName))
					return;
				log.debug("Store list {0}::{1} {2}", getQualifiedClassName(owner), owner.proxyKey, propertyName);
				list.addEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange, false, 0, true);
				listTable[list]=new PropertyReference(propertyName, owner);
			}
			if (list is ArrayCollection && ArrayCollection(list).list is ManagedArrayList)
			{
				ManagedArrayList(ArrayCollection(list).list).serverCallsEnabled = false;
			}
			for (var i:int=0; i < list.length; i++)
			{
				if (list.getItemAt(i) is IHibernateProxy)
				{
					var proxy:IHibernateProxy=list.getItemAt(i) as IHibernateProxy;
					if (!contains(proxy))
					{
						if (isNew(proxy))
						{
							addNewObject(proxy)
						}
						else
						{
							store(proxy);
						}
					}
				}
			}
			if (list is ArrayCollection && ArrayCollection(list).list is ManagedArrayList)
			{
				ManagedArrayList(ArrayCollection(list).list).serverCallsEnabled = false;
			}
		}

		/*
		   public static function hasChanges( object : IHibernateProxy , generator : ChangeMessageGenerator = null ) : Boolean
		   {
		   if ( !contains( object ) ) return false;
		   if ( !generator ) generator = new ChangeMessageGenerator();
		   var changes : Array = generator.getChanges( object , true );
		   for each ( var changeMessage : ObjectChangeMessage in changes )
		   {
		   if ( changeMessage.numChanges > 0 ) return true;
		   }
		   return false;
		   }
		 */
		public static function store(object:IHibernateProxy):String
		{
			if (!object)
				return null;
			var objectIsNew:Boolean=isNew(object);
			var key:String=getKey(object);
			if (key == null)
				throw new Error("Null key generated");
			if (containsByKey(key))
			{
				log.info("Store {0} :: REDUNDANT!", key);
				return null;
			}
			if (ClassUtils.isImmutable(object))
			{
				log.info("Store {0} :: Object is immutable - exiting", key)
				return null;
			}
			log.info("Store {0}", key);
			if (!object is IEventDispatcher)
			{
				log.warn("Object is not an EventDispatcher.  Changes won't be detected");
			}
			else
			{
				var dispatcher:IEventDispatcher=IEventDispatcher(object);
//				dispatcher.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, onPropertyChange, false, 0, true);
				dispatcher.addEventListener(LazyLoadEvent.pending, onLazyLoadStart, false, 0, true);
				dispatcher.addEventListener(LazyLoadEvent.complete, onLazyLoadComplete, false, 0, true);
			}
			var descriptor:HibernateProxyDescriptor=new HibernateProxyDescriptor(object);
			changeEntries[key]=new ObjectChangeMessage(descriptor, objectIsNew);
			if (object is IManaged)
			{
				keyToUIDMap[key] = IManaged(object).uid;
			}
			storeComplexProperties(object);
			return key;
		}
		
		/**
		 * Iterates the properties of object, and stores the baseline of all lists and
		 * properties with a type of IHibernateProxy */
		private static function storeComplexProperties(object:IHibernateProxy):void
		{
			// Prevent triggering loads of data
			log.debug("storeChildren {0}::{1}", getQualifiedClassName(object), object.proxyKey);
			var ro:IHibernateRPC=HibernateManaged.getIHibernateRPCForBean(object);
			var reEnableServerCalls:Boolean;
			if (ro.enabled)
			{
				ro.enabled = false;
				reEnableServerCalls=true;
			}
			for each (var accessor:XML in ClassUtils.getAccessors(object))
			{
				var propertyName:String=accessor.@name;
				if (ignoreProperty(object, propertyName))
					continue;

				var child:Object=object[propertyName];
				if (child is IHibernateProxy)
				{
					store(child as IHibernateProxy);
				}
				if (child is IList)
				{
					storeList(child as IList, object, propertyName);
				}
			}
			if (reEnableServerCalls)
			{
				ro.enabled = true;
			}
		}

		internal static function removeFromStore(object:IHibernateProxy, recursionTracker:ArrayCollection=null):void
		{
			log.debug("removeFromSTore {0}::{1}", getQualifiedClassName(object), object.proxyKey);
			var key:String=getKey(object);
			delete changeEntries[key];

			if (!recursionTracker)
				// TODO : Use a hash here instead
				recursionTracker=new ArrayCollection();
			if (recursionTracker.contains(key))
				return;
			recursionTracker.addItem(key);
			resetStateOnChildren(object, recursionTracker);
		}

		internal static function getStoredObject(key:String):IHibernateProxy
		{
			log.debug("getStoredObject {0}", key);
			var entry:ObjectChangeMessage=changeEntries[key];
			if (!entry)
				return null;
			return entry.owner.source;
		}

		internal static function updateKey(oldKey:String, newKey:String, object:IHibernateProxy):void
		{
			var oldValue:ObjectChangeMessage=changeEntries[oldKey];
			if (!oldValue)
				return;

			delete changeEntries[oldKey];
			
			log.info("Updated key for store repository.  Was: {0}  Now: {1}", oldKey, newKey);

			// Because the key has changed, we need to update the owner reference
			var proxyOwner:HibernateProxyDescriptor=new HibernateProxyDescriptor(object);
			oldValue.owner=proxyOwner;
			changeEntries[newKey]=oldValue;
			if (object is IManaged)
			{
				delete keyToUIDMap[oldKey];
				keyToUIDMap[newKey] = IManaged(object).uid;
			}
		}

		private static function resetStateOnChildren(object:IHibernateProxy, recursionTracker:ArrayCollection):void
		{
			log.debug("resetSTateOnChildren {0}::{1}", getQualifiedClassName(object), object.proxyKey);
			if (ClassUtils.isImmutable(object))
			{
				return;
			}
			var children:ArrayCollection=getChildrenValues(object);
			resetStateOnList(children, recursionTracker);
		}

		internal static function resetStateOnList(list:IList, recursionTracker:ArrayCollection):void
		{
			for each (var member:Object in list)
			{
				if (member is IHibernateProxy)
				{
					removeFromStore(member as IHibernateProxy, recursionTracker);
				}
				if (member is IList)
				{
					resetStateOnList(member as IList, recursionTracker)
				}
			}
		}


		public static function contains(object:IHibernateProxy):Boolean
		{
			var key:String = getKey(object);
			var containsForEntity:Boolean = containsByKey(key);
			if (containsForEntity && object is IManaged)
			{
				var containsForInstance:Boolean = keyToUIDMap[key] == IManaged(object).uid;
				if (containsForEntity && !containsForInstance)
				{
					log.warn("Entity instance mismatch.  State repository contains an entity for " + key + " but it is not the instance passed.  Changes are tracked on a single instance only.");
					return false;
				}
			}
			return containsForEntity;
			
		}

		internal static function containsByKey(key:String):Boolean
		{
			return changeEntries[key] != null;
		}

		public static function reset():void
		{
			changeEntries=new Object();
			listTable=new Dictionary();
		}

		public static function getKey(object:IHibernateProxy):String
		{
			return ClassUtils.getRemoteClassName(object) + "_" + object.proxyKey;
		}
		public static function getKeyForDescriptor(descriptor:IHibernateProxyDescriptor):String
		{
			return descriptor.remoteClassName + "_" + descriptor.proxyId;
		}

		public static function hasChangedProperty(object:IHibernateProxy, propertyName:String):Boolean
		{
			if (!contains(object))
				return false;
			var changeMessage:ObjectChangeMessage=getStoredChanges(object);
			if (!changeMessage)
				return false;
			return changeMessage.hasChangedProperty(propertyName);
			//return ChangeMessageGenerator.getChangesForEntityOnly(object).hasChangedProperty(propertyName);
		}
		
		public static function saveCompleted(objectChangeMessage:ObjectChangeMessage):void
		{
			var targetDescriptor:IHibernateProxyDescriptor = objectChangeMessage.owner;
			var target:IHibernateProxy = targetDescriptor.source;
			var key:String = getKey(target);
			log.debug("saveCompleted: {0}",key);	
			var sourceChangeMessage:ObjectChangeMessage = changeEntries[key];
			sourceChangeMessage.markPropertyChangeMessagesUpdated(objectChangeMessage.changedProperties);
		}

		public static function saveStarted(object:IHibernateProxy):void
		{
			/*
			var key:String=getKey(object);
			savePendingEntities[key]=object;
			// Store a baseline for changes that occur while save is in progress
			store(object);
			*/
		}

		internal static function hasPendingSave(proxy:IHibernateProxy):Boolean
		{
			return false;
			/*
			return hasPendingSaveByKey(getKey(proxy));
			*/
		}

		private static var changesRecursionDictionary:Dictionary=new Dictionary(true);

		internal static function getStoredChanges(object:IHibernateProxy):ObjectChangeMessage
		{
			log.debug("getSToredChanges {0}::{1}", getQualifiedClassName(object), object.proxyKey);
			var key:String=getKey(object);
			return changeEntries[key];
		}

		private static function onPropertyChange(event:PropertyChangeEvent):void
		{
			if ( ignorePropertyChanges ) return;
			if (!(event.source is IHibernateProxy))
			{
				throw new Error("Got PropertyChange event on non IHibernateProxy");
				return;
			}
			var proxy:IHibernateProxy=event.source as IHibernateProxy;
			storeChange(proxy, event.property as String, event.oldValue, event.newValue);
		}

		public static function storeChange(proxy:IHibernateProxy, propertyName:String, oldValue:Object, newValue:Object):void
		{
			if (ignorePropertyChanges)
				return;
			if (ignoreProperty(proxy, propertyName))
				return;
			if (isLazyLoading(proxy))
				return;
			log.debug("storeChange {0}::{1} {2}", getQualifiedClassName(proxy), proxy.proxyKey, propertyName);
			var changes:ObjectChangeMessage=getStoredChanges(proxy);
			if (!changes)
			{
				log.error("Cannot store change without base change position.  Something's wrong!");
				return;
			}
			var propertyChangeMessage:PropertyChangeMessage = getPropertyChangeMessage(propertyName,oldValue,newValue);
			changes.addChange(propertyChangeMessage);
			if (oldValue is IList || newValue is IList)
			{
				updateListReferences(oldValue as IList, newValue as IList, proxy, propertyName);
			}
		}

		private static function updateListReferences(oldValue:IList, newValue:IList, owner:IHibernateProxy, propertyName:String):void
		{
			log.debug("updateListReferences {0}::{1} {2}", getQualifiedClassName(owner), owner.proxyKey, propertyName);
			if (oldValue)
			{
				delete listTable[oldValue];
				oldValue.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange)
			}
			storeList(newValue, owner, propertyName);
		}

		internal static function getMutableChildrenValues(object:IHibernateProxy):ArrayCollection // of Object
		{
			return getChildrenValues(object,true);
		}
		// TODO : This is a util method.  Refactor
		internal static function getChildrenValues(object:IHibernateProxy,excludeImmutable:Boolean=false):ArrayCollection // of Object
		{
			log.debug("getChildrenValue {0}::{1}", getQualifiedClassName(object), object.proxyKey);
			var result:ArrayCollection=new ArrayCollection();
			// Prevent triggering loads of data
			var ro:IHibernateRPC=HibernateManaged.getIHibernateRPCForBean(object);
			var reEnableServerCalls:Boolean;
			if (ro.enabled)
			{
				ro.enabled = false;
				reEnableServerCalls=true;

			}
			for each (var accessor:XML in ClassUtils.getAccessors(object))
			{
				var propertyName:String=accessor.@name;
				if (ignoreProperty(object, propertyName))
					continue;

				var child:Object=object[propertyName];
				if (child)
					result.addItem(child);
			}
			if (reEnableServerCalls)
			{
				ro.enabled = true;
			}
			return result;
		}

		private static function getPropertyChangeMessage(propertyName:String, oldValue:Object, newValue:Object):PropertyChangeMessage
		{
			log.debug("getPropertyChangeMessage: {0}", propertyName);
			if (newValue is IList)
			{
				return getCollectionChangeMessage(propertyName, newValue as IList);
			}
			if (oldValue is IHibernateProxy)
			{
				oldValue=new HibernateProxyDescriptor(oldValue as IHibernateProxy);
			}
			if (newValue is IHibernateProxy)
			{
				newValue=new HibernateProxyDescriptor(newValue as IHibernateProxy);
			}
			var propertyChangeMessage:PropertyChangeMessage=new PropertyChangeMessage(propertyName, oldValue, newValue);
			return propertyChangeMessage;
		}

		private static function getCollectionChangeMessage(propertyName:String, list:IList):CollectionChangeMessage
		{
			log.debug("getCollectionChangeMessage {0} ", propertyName);
			var message:CollectionChangeMessage=new CollectionChangeMessage(propertyName, list);
			return message;
		}

		private static function onCollectionChange(event:CollectionEvent):void
		{
			log.debug("onCollectionChange : {0} ", event.kind);
			if (event.kind == CollectionEventKind.ADD)
			{
				log.debug("onCollectionChange - ADD");
				for each (var item:IHibernateProxy in event.items)
				{
					generateFullChangeMessage(item);
					// For testing:
					if (!contains(item))
					{
						throw new Error("Assertion failed: Added item, but contains == false");
					}
				}
			}
			var list:IList=event.target as IList;
			var propertyReference:PropertyReference=listTable[list];
			// For collections, we don't store the old value
			if (event.kind == CollectionEventKind.ADD || event.kind == CollectionEventKind.REMOVE || event.kind == CollectionEventKind.REPLACE)
			{
				log.debug("onCollectionChange - Calling storeChange as a result of event kind {0}",event.kind);
				storeChange(propertyReference.owner, propertyReference.propertyName, null, list);
			}
		}

		public static function addNewObject(object:IHibernateProxy):void
		{
			log.debug("addNewObject {0}::{1}", getQualifiedClassName(object), object.proxyKey);
			generateFullChangeMessage(object);
		}

		private static function generateFullChangeMessage(item:IHibernateProxy):void
		{
			log.debug("generateFullChangeMessage {0}::{1}", getQualifiedClassName(item), item.proxyKey);
			var key:String=store(item);
			// TODO : Isn't all this duplication?  Haven't we just stored this all?
			for each (var accessor:XML in ClassUtils.getAccessors(item))
			{
				var propertyName:String=accessor.@name;
				if (!ignoreProperty(item, propertyName))
				{
					var property:Object=item[propertyName];
					if (property is IHibernateProxy)
					{
						var proxy:IHibernateProxy=property as IHibernateProxy;
						if (isNew(proxy) && !contains(proxy))
						{
							addNewObject(proxy);
						}
					}
					storeChange(item, propertyName, null, property);
				}
			}
		}

		public static function isNew(object:IHibernateProxy):Boolean
		{
			return newEntities.hasOwnProperty(object.proxyKey);
		}

		public static function removeFromNewEntityList(oldKey:String):void
		{
			delete newEntities[oldKey];
		}
		private static var _ignoredProperties:Object={proxyInitialized: null, proxyKey: null};

		private static function ignoreProperty(object:Object, propertyName:String):Boolean
		{
			if (_ignoredProperties.hasOwnProperty(propertyName))
				return true;
			if (ClassUtils.isImmutable(object))
				return true;
			if (ClassUtils.isTransient(object, propertyName))
				return true;
			if (ClassUtils.isPropertyImmutable(object, propertyName))
				return true;
			return false
		}


		private static function onLazyLoadStart(event:LazyLoadEvent):void
		{
			var proxy:IHibernateProxy=event.target as IHibernateProxy;
			var key:String=getKey(proxy);
			lazyLoadingEntities[key]=true;
		}

		private static function onLazyLoadComplete(event:LazyLoadEvent):void
		{
			var proxy:IHibernateProxy=event.target as IHibernateProxy;
			var key:String=getKey(proxy);
			delete lazyLoadingEntities[key];

			// TODO : This seems a bit heavy handed - resetting on an object when it's lazy load has finished.
			// It could be just for a property
			removeFromStore(proxy);
			store(proxy);
		}

		private static function isLazyLoading(object:IHibernateProxy):Boolean
		{
			var key:String=getKey(object);
			return isLazyLoadingByKey(key);
		}

		private static function isLazyLoadingByKey(key:String):Boolean
		{
			return lazyLoadingEntities[key] != null && lazyLoadingEntities[key] == true;
		}

		public static function getKeyForNewObject():Object
		{
			var key:String=UIDUtil.createUID();
			newEntities[key]=key;
			return key;
		}
		public static function newObjectSaveCompleted(changeResult:ObjectChangeResult):void
		{
			var oldKey : String = changeResult.remoteClassName + "_" + changeResult.oldId;
			var proxy : IHibernateProxy = getStoredObject( oldKey );
			if ( proxy )
			{
				proxy.proxyKey = changeResult.newId;
				var newKey : String = getKey( proxy );
				updateKey( oldKey , newKey , proxy );
				var changeMessage:ObjectChangeMessage = getStoredChanges( proxy );
				changeMessage.setIsNotNew();
			} else {
				log.error( "Cannot find proxy in StoreRepository with key {0} to update.  (New id from save operation is {1})",oldKey,changeResult.newId);
			}
		}
		
		public static function applyChangeMessage(changeMessage:ObjectChangeMessage):void
		{
			var entityManager:IEntityManager = HibernateManaged.entityManager;
			if (!entityManager)
				throw new Error("No entity manager found");
			
			var entity:IHibernateProxy = entityManager.findForDescriptor(changeMessage.owner);
			if (!entity)
			{
				log.error("Cannot apply change message for entity " + getKeyForDescriptor(changeMessage.owner) + " as it was not found in the EntityManager");
				return;
			}
			
			var oldIgnorePropertyChanges:Boolean = ignorePropertyChanges;
			ignorePropertyChanges = true;
			
			// TODO : Detect conflicts on the client..
			
			for each (var propertyChange:PropertyChangeMessage in changeMessage.changedProperties)
			{
				applyPropertyChangeMessage(entity,propertyChange);
			}
			
			ignorePropertyChanges = oldIgnorePropertyChanges;
		}
		protected static function applyPropertyChangeMessage(entity:IHibernateProxy,propertyChange:PropertyChangeMessage):void
		{
			var propertyName:String = propertyChange.propertyName;
			// This needs some fleshing out...
			entity[propertyName] = propertyChange.newValue;
		}

	}
}
import org.dphibernate.core.IHibernateProxy;

class PropertyReference
{
	public var propertyName:String;
	public var owner:IHibernateProxy;

	public function PropertyReference(propertyName:String, owner:IHibernateProxy)
	{
		this.propertyName=propertyName;
		this.owner=owner;
	}
}