/**
   Copyright (c) 2008. Digital Primates IT Consulting Group
   http://www.digitalprimates.net
   All rights reserved.

   This library is free software; you can redistribute it and/or modify it under the
   terms of the GNU Lesser General Public License as published by the Free Software
   Foundation; either version 2.1 of the License.

   This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
   See the GNU Lesser General Public License for more details.


   @author: malabriola
   @ignore
 **/

package org.dphibernate.rpc
{

	import com.hexagonstar.util.debug.StopWatch;
	
	import flash.events.IEventDispatcher;
	import flash.sampler.DeleteObjectSample;
	import flash.utils.*;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.core.IPropertyChangeNotifier;
	import mx.events.PropertyChangeEvent;
	import mx.logging.ILogger;
	import mx.rpc.AsyncToken;
	import mx.rpc.Responder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.utils.DescribeTypeCache;
	import mx.utils.DescribeTypeCacheRecord;
	import mx.utils.ObjectUtil;
	import mx.utils.StringUtil;
	
	import org.dphibernate.collections.ManagedArrayList;
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.events.LazyLoadEvent;
	import org.dphibernate.persistence.state.StateRepository;
	import org.dphibernate.util.BeanUtil;
	import org.dphibernate.util.LogUtil;

	public class HibernateManaged
	{
		public static const PROXY_LOAD_METHOD:String="loadDPProxy";

		public static const PROXY_SAVE_METHOD:String="saveDPProxy";

		public static var hibernateRPCProvider:IHibernateROProvider=new DefaultHibernateRPCProvider();
		public static var entityManager:IEntityManager;

		protected static var recursionWatch:Dictionary=new Dictionary(true);

		protected static var objectsCurrentLoading:Object = new Object(); // Hash - stored by unique key generated for proxy

		private static var log:ILogger=LogUtil.getLogger(HibernateManaged);

		// TODO : This belongs on DefaultHibernateRPCProvider
		// However, moving it makes config uglier.
		public static var defaultHibernateService:IHibernateRPC;

		public static function getIHibernateRPCForBean(obj:IHibernateProxy):IHibernateRPC
		{
			return hibernateRPCProvider.getRemoteObject(obj);
		}

		public static function manageChildTree(object:Object, parent:Object=null, propertyName:String=null):void
		{
			recursionWatch=new Dictionary(true);
			manageChildHibernateObjects(object, parent, propertyName);
			recursionWatch=new Dictionary(true);
		}

		private static function manageChildHibernateObjects(object:Object, parent:Object=null, propertyName:String=null):void
		{
			// Check to see if the object is in the entityManager
			if (parent && propertyName && entityManager && object is IHibernateProxy && entityManager.containsEntity(object as IHibernateProxy))
			{
				var entityManagerInstance:IHibernateProxy = entityManager.getEntity(object as IHibernateProxy);
				parent[propertyName] = entityManagerInstance;
				return;
			}

			// TODO : MP : From what I can tell, we walk the full tree here, recursively
			// calling manageChildHibernateObjects().  However, almost everything
			// that was performed here is now not required.
			// The one remaining task is that we find ArrayCollections
			// and turn them into Managed array collections if they are paginated.
			// Need to work out a way to do that which doesn't involve this recursion
			//
			// What if we change the type sent by the server for Paginated collections
			// from mx.collections.ArrayCollection to org.dphibernate.collections.ManagedCollection
			// (given ManagedCollection extends ArrayCollection)
			// Then, we don't need any of this.
			
				
			var entry:XML;
			var accessors:XMLList

			if (!object)
			{
				return;
			}

			if (recursionWatch[object] == true)
			{
				//we have already examined this object, get out!
				return;
			}

			recursionWatch[object]=true;

			if (!(object is String))
			{
				var cacheRecord:DescribeTypeCacheRecord=DescribeTypeCache.describeType(object);
				entry=cacheRecord.typeDescription;
				accessors=entry.accessor;
			}

			if (object is IHibernateProxy)
			{
				var proxy:IHibernateProxy = IHibernateProxy(object);
				manageHibernateObject(proxy, parent);

				if (entityManager)
					updateEntityManager(proxy);
				if (proxy.proxyInitialized)
				{
					for (var k:int=0; k < accessors.length(); k++)
					{
						var accessor:Object=accessors[k];
						if (accessor.metadata.(@name == "Transient").length() > 0 || accessor.@access == "readonly")
						{
							// Skip, because it's transient
//                            log.info("Not managing {0}.{1} because transient or readonly", accessor.@declaredBy, accessor.@name);
						}
						else
						{
							manageChildHibernateObjects(object[accessors[k].@name], object, accessors[k].@name);
						}
					}
				}
			}
			else if (object is ArrayCollection)
			{
				manageArrayCollection(ArrayCollection(object));
			}
			else if (!ObjectUtil.isSimple(object))
			{
				for (var j:int=0; j < accessors.length(); j++)
				{
					manageChildHibernateObjects(object[accessors[j].@name], object, accessors[j].@name);
				}
			}
		}
		protected static function updateEntityManager(object:Object):void
		{
			if (!object) return;
			if (!(object is IHibernateProxy)) return;
			var proxy:IHibernateProxy = object as IHibernateProxy;
			if (!entityManager.containsEntity(proxy))
			{
				entityManager.putEntity(proxy);
			} else {
				trace("WARNING:  Received updated entity from server when client version already exists.  Entity merging not yet supported");
			}
		}
		public static function manageArrayCollection(collection:ArrayCollection):void
		{
			if (collection.list is ManagedArrayList)
			{
				ManagedArrayList(collection.list).serverCallsEnabled=false;
			}
			var isPagedCollection:Boolean=false;
			for (var i:int=0; i < collection.length; i++)
			{
				var collectionMember:Object=collection[i];
				manageChildHibernateObjects(collectionMember, collection, String(i))
				if (collectionMember is IHibernateProxy && IHibernateProxy(collectionMember).proxyInitialized == false)
				{
					isPagedCollection=true;
				}
			}
			if (isPagedCollection)
			{
				var managedArrayList:ManagedArrayList=new ManagedArrayList(collection.source);
				collection.list=managedArrayList;
			}
			if (collection.list is ManagedArrayList)
			{
				ManagedArrayList(collection.list).serverCallsEnabled=true;
			}
		}

		public static function manageHibernateObject(obj:IHibernateProxy, parent:Object):void
		{
			if ((obj is IPropertyChangeNotifier) && parent)
			{
				(obj as IPropertyChangeNotifier).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, parent.dispatchEvent);
			}
		}

		protected static function getLazyDataFromServer(obj:IHibernateProxy, property:String, value:*=null):*
		{
			var lazyLoadResponder:Responder=new Responder(HibernateManaged.lazyLoadArrived, HibernateManaged.lazyLoadFailed);

			var token:AsyncToken;
			var remoteObject:IHibernateRPC=hibernateRPCProvider.getRemoteObject(obj);
			token=remoteObject.loadProxy(obj.proxyKey, obj);

			token.ro=remoteObject;
			token.addResponder(lazyLoadResponder);
			token.obj=obj;
			token.property=property;
			token.ro=remoteObject;

			token.oldValue=obj;
			/*
			   token.parent=hibernateDictionary[obj].parent;
			   token.parentProperty=hibernateDictionary[obj].parentProperty;
			 */
			var key:String = StateRepository.getKey(obj);
			var uidKey:String = "";
			if (Object(obj).hasOwnProperty("uid"))
			{
				uidKey = "  - Uid: " + obj["uid"];
			}
			trace("Asking for Lazy Data for Property " + property + " on " + key + uidKey);


			if (obj is IEventDispatcher)
			{
				IEventDispatcher(obj).dispatchEvent(new LazyLoadEvent(LazyLoadEvent.pending, property, true, true));
			}

			//this is where we need to be cautious. We either need to give back a simple value or another proxy
			if (property)
			{
				return value;
			}
			return obj;
		}

		public static function getProperty(obj:IHibernateProxy, property:String, value:*):*
		{
			// TODO : MP - trying this, but not sure if it's right.  We have the value, so lets just return it,
			// doesn't matter if the proxy is initialized or not.
			if (value)
				return value;
			if (obj.proxyInitialized)
				return value;

			var ro:IHibernateRPC=hibernateRPCProvider.getRemoteObject(obj);
			if (!isCurrentlyLoading(obj) && ro.enabled)
			{
				setObjectLoading(obj);
				return getLazyDataFromServer(obj, property, value);
			}
			else
			{
				return value;
			}
		}

		private static function setObjectLoading(obj:IHibernateProxy):void
		{
			var key:String = StateRepository.getKey(obj);
			objectsCurrentLoading[key] = true;
		}

		private static function isCurrentlyLoading(obj:IHibernateProxy):Boolean
		{
			// TODO : getKey should be moved off StateRepository
			var key:String = StateRepository.getKey(obj);
			return objectsCurrentLoading[key] != null;
		}
		private static function setObjectLoadComplete(proxy:IHibernateProxy):void
		{
			var key:String = StateRepository.getKey(proxy);
			delete objectsCurrentLoading[key];
		}

		public static function rebroadcastEvent(event:PropertyChangeEvent):void
		{
			trace(event.property + " propogating ");
		}

		public static function setProperty(obj:IHibernateProxy, property:Object, oldValue:*, newValue:*, parent:Object=null):void
		{

			var dispatcher:IEventDispatcher=obj as IEventDispatcher;

			if ((oldValue is IPropertyChangeNotifier) && parent)
			{
				oldValue.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, parent.dispatchEvent);
			}

			if ((newValue is IPropertyChangeNotifier) && parent)
			{
				newValue.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, parent.dispatchEvent);
			}

			if (dispatcher) // && (dispatcher.hasEventListener(PropertyChangeEvent.PROPERTY_CHANGE)) )
			{
				var event:PropertyChangeEvent=PropertyChangeEvent.createUpdateEvent(dispatcher, property, oldValue, newValue);
				dispatcher.dispatchEvent(event);
			}
		}

		public static function lazyLoadArrived(event:ResultEvent):void
		{
			var methodSw:StopWatch=StopWatch.startNew("lazyLoadArrived");
			var token:AsyncToken=event.token;

			setObjectLoadComplete(token.obj);

			var classDef:Class=getDefinitionByName(getQualifiedClassName(token.obj)) as Class;
			var ro:IHibernateRPC=token.ro;
			ro.enabled=false;
			token.obj.proxyInitialized=true;

			StateRepository.ignorePropertyChanges=true;
			var sw:StopWatch=StopWatch.startNew("BeanUtil.populateBean");
			BeanUtil.populateBean(event.result, classDef, token.obj, new Dictionary(true), token.parent);
			sw.stopAndTrace();
			StateRepository.ignorePropertyChanges=false;
			ro.enabled=true;
			setProperty(token.obj, token.property, token.oldValue, event.result, token.parent)

			if (token.obj is IEventDispatcher)
			{
				IEventDispatcher(token.obj).dispatchEvent(new LazyLoadEvent(LazyLoadEvent.complete, token.parentProperty, token.parent, true, true));
			}
			methodSw.stopAndTrace();
		}

		public static function lazyLoadFailed(event:FaultEvent):void
		{
			var token:AsyncToken=event.token;
			var entity:Object = token.obj; 
			var errorMessage:String = StringUtil.substitute("Attempt to load {0} id {1} (requested property: {2}) failed the following error: \n\t{3}",getQualifiedClassName(entity),IHibernateProxy(entity).proxyKey.toString(),token.property,event.fault.faultString)
			trace(errorMessage);


			if (token && token.obj && token.obj is IEventDispatcher)
			{
				IEventDispatcher(token.obj).dispatchEvent(new LazyLoadEvent(LazyLoadEvent.failed, token.parentProperty, token.parent, true, true));
			}
		}

		public static function addHibernateResponder(ro:IHibernateRPC, token:AsyncToken):void
		{
			var managedResponder:Responder=new Responder(handleHibernateResult, handleHibernateFault);
			token.ro=ro;
			token.addResponder(managedResponder);
		}

		/**
		 * This method is called when any arbitary remote method invocation has completed.
		 * For lazyLoad operations, see lazyLoadArrived */
		protected static function handleHibernateResult(event:ResultEvent):void
		{
			var sw:StopWatch=StopWatch.startNew("handleHibernateResult");
			var remoteService:IHibernateRPC=IHibernateRPC(event.token.ro);
			remoteService.enabled=false;
			manageChildTree(event.result, null, null);
			if (remoteService.stateTrackingEnabled)
			{
				manageStateOfResultObject(event);
			}
			remoteService.enabled=true;
			sw.stopAndTrace();
		}

		protected static function manageStateOfResultObject(event:ResultEvent):void
		{
			if (event.result is IHibernateProxy)
			{
				StateRepository.store(event.result as IHibernateProxy);
			}
			else if (event.result is IList)
			{
				StateRepository.storeList(event.result as IList);
			}
		}

		protected static function handleHibernateFault(event:FaultEvent):void
		{
			log.error("Fault when attempting dpHibernate operation: " + event.fault.faultString);
		}


	}
}
