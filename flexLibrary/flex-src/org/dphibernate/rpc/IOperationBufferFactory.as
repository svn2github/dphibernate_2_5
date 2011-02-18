package org.dphibernate.rpc
{
	import mx.rpc.AbstractOperation;
	import mx.rpc.remoting.RemoteObject;

	public interface IOperationBufferFactory
	{
		function getBuffer(remoteObject:RemoteObject, operation:AbstractOperation):IOperationBuffer
	}
}