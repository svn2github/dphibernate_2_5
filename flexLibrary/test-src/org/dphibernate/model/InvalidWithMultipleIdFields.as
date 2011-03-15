package org.dphibernate.model
{
	[Entity]
	public class InvalidWithMultipleIdFields
	{
		[Id]
		public var id:int;
		
		[Id]
		public var key:String;
		
	}
}