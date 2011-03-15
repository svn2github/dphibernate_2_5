package org.dphibernate.persistence.state
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	import org.dphibernate.core.IEntity;

	public interface IEntityUpdater
	{
		function save( object : IEntity , responder : IResponder = null ) : AsyncToken;
		function deleteRecord( object : IEntity , responder:IResponder = null) : AsyncToken;
	}
}