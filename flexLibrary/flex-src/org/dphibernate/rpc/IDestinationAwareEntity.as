package org.dphibernate.rpc
{
	import org.dphibernate.core.IEntity;

	public interface IDestinationAwareEntity extends IEntity
	{
		function get destinationName():String;
		function set destinationName(value:String):void;
	}
}