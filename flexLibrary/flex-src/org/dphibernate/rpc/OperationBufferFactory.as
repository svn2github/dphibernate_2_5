package org.dphibernate.rpc
{
	import flash.utils.Dictionary;
	
	import mx.rpc.AbstractOperation;
	import mx.rpc.remoting.RemoteObject;
	
	public class OperationBufferFactory implements IOperationBufferFactory
	{
		private var initialDelay:int;
		private var maxDelay:int;
		private var opreationsToBuffer:Array;
		private var operationBuffers:Dictionary = new Dictionary(); // of Operation,OperationBuffer
		// An object which contains the names of the operations we're buffering
		private var operationNameHash:Object = new Object();
		public function OperationBufferFactory(operationsNamesToBuffer:Array,initialDelay:int,maxDelay:int)
		{
			this.initialDelay = initialDelay;
			this.maxDelay = maxDelay;
			this.opreationsToBuffer = operationsNamesToBuffer;
			initalizeOperationNameHash();
		}
		public function getBuffer(remoteObject:RemoteObject,operation:AbstractOperation):IOperationBuffer
		{
			if (!shouldBuffer(operation))
				return null;
			var buffer:IOperationBuffer = operationBuffers[operation];
			if (!buffer || ( buffer && buffer.acceptingNewRequests == false) )
			{
				buffer = new LoadProxyRequestBuffer(remoteObject,initialDelay,maxDelay);
				operationBuffers[operation] = buffer;
					
			}
			return buffer;
		}
		private function shouldBuffer(operation:AbstractOperation):Boolean
		{
			return operationNameHash.hasOwnProperty(operation.name);
		}
		/**
		 * Translates the array of operation names
		 * into an object for fast look-up at runtime */
		private function initalizeOperationNameHash():void
		{
			operationNameHash = new Object();
			for each (var operationName:String in opreationsToBuffer)
			{
				operationNameHash[operationName] = operationName;
			}
		}
	}
}