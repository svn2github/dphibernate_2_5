package org.dphibernate.rpc
{
	import org.dphibernate.core.IHibernateProxy;

	public interface IDestinationAwareHibernateProxy extends IHibernateProxy
	{
		function get destinationName():String;
		function set destinationName(value:String):void;
	}
}