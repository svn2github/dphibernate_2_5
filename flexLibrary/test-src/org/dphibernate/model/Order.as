package org.dphibernate.model
{
	import mx.collections.ArrayCollection;

	[Entity]
	[RemoteClass(alias="remote.Order")]
	public class Order
	{
		public function Order(id:int)
		{
			this.id = id;
		}
		[Id]
		public var id:int;
		public var customer:Customer;
		public var products:ArrayCollection;
	}
}