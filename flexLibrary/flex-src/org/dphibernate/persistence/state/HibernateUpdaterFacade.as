package org.dphibernate.persistence.state
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	import org.dphibernate.core.IHibernateProxy;
	/**
	 * Simple wrapper for the static methods of the HibernateUpdater
	 * */
	public class HibernateUpdaterFacade implements IHibernateUpdater
	{
		public function HibernateUpdaterFacade()
		{
		}
		
		public function save(object:IHibernateProxy, responder:IResponder=null):AsyncToken
		{
			return HibernateUpdater.save(object,responder);
		}
		
		public function deleteRecord(object:IHibernateProxy, responder:IResponder=null):AsyncToken
		{
			return HibernateUpdater.deleteRecord(object,responder);
		}
	}
}