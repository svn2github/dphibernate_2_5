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
    
    import flash.utils.*;
    
    import mx.core.mx_internal;
    import mx.logging.ILogger;
    import mx.rpc.AbstractOperation;
    import mx.rpc.AsyncToken;
    import mx.rpc.Responder;
    import mx.rpc.events.FaultEvent;
    import mx.rpc.events.ResultEvent;
    import mx.rpc.remoting.mxml.RemoteObject;
    
    import org.dphibernate.core.IHibernateProxy;
    import org.dphibernate.util.ClassUtils;
    import org.dphibernate.util.LogUtil;

    use namespace flash_proxy;
    use namespace mx_internal;

    use namespace flash_proxy;

    dynamic public class HibernateRemoteObject extends RemoteObject implements IHibernateRPC
    {
		private var log : ILogger = LogUtil.getLogger( this );
		private var loadingProxies:Object = new Object();
		
		private var _enabled:Boolean = true;
		public function get enabled():Boolean
		{
			return _enabled;
		}
		public function set enabled(value:Boolean):void
		{
			_enabled = value;
		}
		public var operationBufferFactory:IOperationBufferFactory;
		
		/**
		 * A method which is invoked after the operation has been
		 * constructed.  Allows for modifying an operation, before
		 * it's used for anything.
		 * Useful for testing.
		 * 
		 * Method must be of type:
		 * function(operation:Operation):void {}
		 * */
		internal var operationPostConstructDecorator:Function;
		
        public function HibernateRemoteObject(destination:String = null,operationBufferFactory:IOperationBufferFactory=null)
        {
            super(destination);
//			requestBuffer = new LoadProxyRequestBuffer(this,50,350);
			this.operationBufferFactory = operationBufferFactory;
        }
		
		override public function getOperation(name:String):AbstractOperation
		{
			var operation:AbstractOperation = super.getOperation(name);
			assignOperationManager(operation);
			if (operationPostConstructDecorator != null)
			{
				operationPostConstructDecorator(operation);
			}
			
			return operation;
		}

		private function assignOperationManager(operation:AbstractOperation):void
		{
			if (!operationBufferFactory || !bufferProxyLoadRequests)
			{
				assignPerCallOperationManager(operation);
			} else {
				assignBufferedOperationManager(operation);
			}
		}
		/**
		 * Assigns an operation manager to handle calls that are not buffered
		 * */
		private function assignPerCallOperationManager(operation:AbstractOperation):void
		{
			LazyLoadingOperationManager.intialize(operation,this);
		}
		/**
		 * Assigns an operation manager to buffer calls
		 * */
		private function assignBufferedOperationManager(operation:AbstractOperation):void
		{
			var buffer:IOperationBuffer = operationBufferFactory.getBuffer(this,operation);
			if (buffer)
			{
				operation.operationManager = buffer.bufferedSend;
			}
		}
		

        public function loadProxy(proxyKey:Object, hibernateProxy:IHibernateProxy):AsyncToken
        {
        	var className : String =  getQualifiedClassName( hibernateProxy );
			var qualifiedProxyKey:String = getQualifiedProxyKey(className,proxyKey);
			if (isProxyLoading(qualifiedProxyKey))
			{
				return getTokenForLoadingProxy(qualifiedProxyKey);
			}
			
        	log.info( "Hydrating proxy {0} id: {1}" , className , proxyKey );
        	var remoteClassName : String = ClassUtils.getRemoteClassName( hibernateProxy );
            var token : AsyncToken = this.loadDPProxy(proxyKey, remoteClassName);
			token.addResponder(new Responder(onProxyLoadComplete,onProxyLoadFault));
			setProxyLoading(qualifiedProxyKey,token);
			token.qualifiedProxyKey = qualifiedProxyKey;
			return token;
        }
		private function getQualifiedProxyKey(className:String,proxyKey:Object):String
		{
			return className + proxyKey.toString();
		}
		private function isProxyLoading(qualifiedProxyKey:String):Boolean
		{
			return loadingProxies.hasOwnProperty(qualifiedProxyKey);
		}
		private function setProxyLoading(qualifiedProxyKey:String,token:AsyncToken):void
		{
			loadingProxies[qualifiedProxyKey] = token;
		}
		private function setProxyLoaded(qualifiedProxyKey:String):void
		{
			delete loadingProxies[qualifiedProxyKey];
		}
		private function getTokenForLoadingProxy(qualifiedProxyKey:String):AsyncToken
		{
			return loadingProxies[qualifiedProxyKey] as AsyncToken;
		}
		public function saveProxy( hibernateProxy : IHibernateProxy , objectChangeMessages : Array ) : AsyncToken
		{
			var className : String = getQualifiedClassName( hibernateProxy );
			log.info( "Saving {0} {1}" , className , hibernateProxy.proxyKey );
			return this.saveDPProxy( objectChangeMessages );
		}
		
		private var _stateTrackingEnabled : Boolean = false;
		public function get stateTrackingEnabled() : Boolean
		{
			return _stateTrackingEnabled;
		}
		public function set stateTrackingEnabled( value : Boolean ) : void
		{
			_stateTrackingEnabled = value;
		}
		
		private var _bufferRequests:Boolean;
		public function get bufferProxyLoadRequests():Boolean
		{
			return _bufferRequests;
		}
		public function set bufferProxyLoadRequests(value:Boolean):void
		{
			_bufferRequests = value;
			if ( bufferProxyLoadRequests && operationBufferFactory == null )
			{
				// Initalize with default;
				operationBufferFactory = new LoadDPProxyOperationBufferFactory();
			}
		}
		private function onProxyLoadComplete(event:ResultEvent):void
		{
			var stopwatch:StopWatch = StopWatch.startNew("onProxyLoadComplete");
			var key:String = event.token.qualifiedProxyKey;
			setProxyLoaded(key);
			stopwatch.stopAndTrace();
		}
		private function onProxyLoadFault(fault:FaultEvent):void
		{
			var key:String = fault.token.qualifiedProxyKey;
			setProxyLoaded(key);
		}
    }
}
import mx.rpc.AbstractOperation;
import mx.rpc.AsyncToken;

import org.dphibernate.rpc.HibernateManaged;
import org.dphibernate.rpc.HibernateRemoteObject;
/**
 * A simple OperationManager.
 * Outbound calls issued through this operation are 
 * intercepted as the AsyncToken is created
 * and wired up to a HibernateResponder,
 * which is responsible for setting up lazy loading
 * on the response received from the server */
class LazyLoadingOperationManager
{
	private var operation:AbstractOperation;
	private var remoteObject:HibernateRemoteObject;
	public function LazyLoadingOperationManager(operation:AbstractOperation,remoteObject:HibernateRemoteObject)
	{
		this.operation = operation;
		this.remoteObject = remoteObject;
		operation.operationManager = managedSend;
	}
	/**
	 * Wraps outbound calls initiated on the operation, to apply
	 * a hibernateResponder to the AsyncToken.
	 * 
	 * This responder is responsible for setting up lazy loading 
	 * on the recieved object.
	 * */
	private function managedSend(...args):AsyncToken
	{
		
		remoteObject.enabled = false;
		// Disable the operation manager temporarily to 
		// get the real async token
		operation.operationManager = null;
		
		var token:AsyncToken = invokeOperationSend(args);
		HibernateManaged.addHibernateResponder(remoteObject, token);
		// Set the operationManager back to us for future calls
		operation.operationManager = managedSend;
		remoteObject.enabled = true;
		return token;
	}
	private function invokeOperationSend(args:Array):AsyncToken
	{
		var methodArgs:Array = args[0];
		if (methodArgs.length == 0)
		{
			return operation.send();
		} else {
			return operation.send.apply(null,methodArgs);
		}
	}
	/**
	 * Convenience constructor to make the syntax more readable
	 * */
	public static function intialize(operation:AbstractOperation,remoteObject:HibernateRemoteObject):LazyLoadingOperationManager
	{
		var manager:LazyLoadingOperationManager = new LazyLoadingOperationManager(operation,remoteObject);
		return manager;
	}
}