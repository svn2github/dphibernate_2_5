package org.dphibernate.rpc
{
	import mx.rpc.AsyncToken;

	public interface IOperationBuffer
	{
		function bufferedSend(...args):AsyncToken;
		function get acceptingNewRequests():Boolean;
	}
}