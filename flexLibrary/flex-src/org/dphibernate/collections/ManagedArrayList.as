package org.dphibernate.collections
{
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	import mx.collections.errors.ItemPendingError;
	import mx.logging.ILogger;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.Responder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.dphibernate.rpc.HibernateManaged;
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.rpc.IHibernateRPC;
	import org.dphibernate.util.LogUtil;
	
	public class ManagedArrayList extends ArrayList
	{
		private var pendingItems : Dictionary = new Dictionary(); // Of AsyncToken,PendingItem
		private var log : ILogger = LogUtil.getLogger(this);
		private var loadingIndexes:Dictionary = new Dictionary(); // Of index,index
		
		public var serverCallsEnabled:Boolean = true;
		
		public function ManagedArrayList(source:Array=null)
		{
			super(source);
		}
		override public function getItemAt(index:int, prefetch:int=0) : Object
		{
			var result : Object = super.getItemAt(index,prefetch);
			if (result is IHibernateProxy && IHibernateProxy(result).proxyInitialized == false)
			{
				handleRemoteItem(IHibernateProxy(result),index,prefetch);
			}
			return result;
		}
		private function handleRemoteItem(proxy:IHibernateProxy,index:int,prefetch:int):void
		{
			// Optomized exit..
			if (indexCurrentlyBeingLoaded(index))
				return;
			if (!serverCallsEnabled)
				return;
			
			var remoteObject : IHibernateRPC = HibernateManaged.getIHibernateRPCForBean( proxy );
			if (remoteObject == null)
			{
				// TODO : Can I recover from this?
				throw new Error("No HibernateRPC object associated with IHibernateProxy.  Ensure that HibernateManaged.defaultRemoteObject is set!");
			}
			// TODO : Handle prefetch
			var token : AsyncToken = remoteObject.loadProxy(proxy.proxyKey,proxy);
			token.addResponder(new Responder(onPendingItemLoaded,onFault));
			var itemPendingError : ItemPendingError = new ItemPendingError("Item is pending");
			var pendingItem:PendingItem = new PendingItem(itemPendingError,index);
			pendingItems[token] = pendingItem;
			setIndexLoading(index);
//			throw itemPendingError;
		}
		
		private function indexCurrentlyBeingLoaded(index:int):Boolean
		{
			return loadingIndexes[index] != null;
		}
		private function setIndexLoading(index:int):void
		{
			loadingIndexes[index] = true;
		}
		private function setIndexLoaded(index:int):void
		{
			delete loadingIndexes[index];
		}
		private function onPendingItemLoaded(data:Object):void
		{
			var resultEvent:ResultEvent = ResultEvent(data);
			var token:AsyncToken = resultEvent.token;
			var pendingItem : PendingItem = pendingItems[token];
			if (!pendingItem)
			{
				log.error("Received result for loaded pending item, but no PendingItem record was waiting!");
				return;
			}
			var result:Object = resultEvent.result;
			setIndexLoaded(pendingItem.index);
			// HACK FOR DEBUGGING...
			try
			{
				this.setItemAt(result,pendingItem.index);
			} catch (e : Error)
			{
				if (e.message == "invalidIndex")
				{
					// This error is thrown by spark.layouts.supportClasses.LinearLayoutVector - it appears to be a bug.
					// Swallow the error and continue
				}
			}
			for each ( var responder : IResponder in pendingItem.error.responders )
			{
				responder.result(data);
			}
			delete pendingItems[token]
		}
		private function onFault(info:Object):void
		{
			var fault : FaultEvent = info as FaultEvent;
			log.error("Fault when trying to load paged collection data - " + fault.fault.faultString);
			if (!fault) return;
			var token:AsyncToken = fault.token;
			var pendingItem : PendingItem = pendingItems[token];
			setIndexLoaded(pendingItem.index);
			for each ( var responder : IResponder in pendingItem.error.responders )
			{
				responder.fault( info );
			}
			delete pendingItems[token]
									
		}

	}
}
import mx.collections.errors.ItemPendingError;

class PendingItem {
	public var error : ItemPendingError;
	public var index : int;
	public function PendingItem(error:ItemPendingError,index:int)
	{
		this.error = error;
		this.index = index;
	}
}