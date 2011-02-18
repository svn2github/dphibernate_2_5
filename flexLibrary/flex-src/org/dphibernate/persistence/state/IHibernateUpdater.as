package org.dphibernate.persistence.state
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	import org.dphibernate.core.IHibernateProxy;

	public interface IHibernateUpdater
	{
		function save( object : IHibernateProxy , responder : IResponder = null ) : AsyncToken;
		function deleteRecord( object : IHibernateProxy , responder:IResponder = null) : AsyncToken;
	}
}