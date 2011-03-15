package org.dphibernate.model
{
	[Entity]
	public class Customer
	{
		public function Customer(id:int)
		{
			this.id = id;
		}
		[Id]
		public var id:int;
		public var name:String;
	}
}