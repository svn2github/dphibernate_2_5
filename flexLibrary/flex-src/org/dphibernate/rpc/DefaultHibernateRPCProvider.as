package org.dphibernate.rpc
{
	import org.dphibernate.rpc.HibernateManaged;
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.rpc.IHibernateRPC;
	import org.dphibernate.rpc.IHibernateROProvider;
	import org.dphibernate.core.IHibernateProxy;

	public class DefaultHibernateRPCProvider implements IHibernateROProvider
	{

		public function getRemoteObject(bean:IHibernateProxy):IHibernateRPC
		{
			return HibernateManaged.defaultHibernateService;
		}
	}
}