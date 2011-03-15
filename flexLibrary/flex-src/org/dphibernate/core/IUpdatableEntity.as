package org.dphibernate.core
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;

	public interface IUpdatableEntity extends IEntity
	{
		function save(responder:IResponder=null) : AsyncToken;
		function deleteRecord(responder:IResponder=null) : AsyncToken;
	}
}