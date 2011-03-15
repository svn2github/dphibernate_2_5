package org.dphibernate.loader
{
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	import org.as3commons.reflect.Type;
	import org.dphibernate.core.EntityStatus;
	import org.dphibernate.core.IEntity;
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.loader.service.ILazyLoadService;

	public class LazyLoader implements ILazyLoader, IResponder
	{

		private var entity:IEntity;
		private var service:ILazyLoadService;
		private var entityResolver:IEntityManager
		public function LazyLoader(entity:IEntity,service:ILazyLoadService,entityResolver:IEntityManager)
		{
			this.entity = entity;
			this.service = service;
			this.entityResolver = entityResolver;
		}
		public function load():AsyncToken
		{
			 // TODO : We may already have the entity on the client
			setEntityStatusToLoading();
			dispatchEvent(LazyLoadEvent.loadingStart());
			
			var remoteClassName:String = getRemoteClassName();
			 
			var token:AsyncToken = service.loadProxy(entity.entityKey,remoteClassName);
			token.addResponder(this);
			
			return token;
		}

		private function dispatchEvent(event:Event):void
		{
			if (entity is IEventDispatcher)
			{
				IEventDispatcher(entity).dispatchEvent(event);
			}
		}

		private function getRemoteClassName():String
		{
			return Type.forInstance(entity).alias;
		}

		private function setEntityStatusToLoading():void
		{
			entity.entityStatus |= EntityStatus.LOADING;
		}
		private function clearEntityLoadingStatus():void
		{
			entity.entityStatus &= ~EntityStatus.LOADING;
		}
		public function result(data:Object):void
		{
			clearEntityLoadingStatus()
			// TODO Auto-generated method stub
			
			dispatchEvent(LazyLoadEvent.loadingCompleted());
		}

		public function fault(info:Object):void
		{
			clearEntityLoadingStatus()
			
			dispatchEvent(LazyLoadEvent.loadingFailed());
		}
	}
}