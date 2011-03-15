package org.dphibernate.model
{
	[Entity]
	public class Product
	{
		public function Product(id:int)
		{
			this._id = id;
		}
		private var _id:int;
		[Id]
		public function get id():int
		{
			return _id;
		}
		public function set id(value:int):void{
			_id = value;
		}
		public var name:String;
	}
}