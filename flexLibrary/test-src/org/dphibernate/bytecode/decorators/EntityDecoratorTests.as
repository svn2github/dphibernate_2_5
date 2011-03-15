package org.dphibernate.bytecode.decorators
{
	import org.dphibernate.ByteCodeRule;
	import org.dphibernate.model.Book;
	import org.dphibernate.model.Customer;
	import org.dphibernate.model.InvalidWithMultipleIdFields;
	import org.dphibernate.model.Product;
	import org.flexunit.asserts.assertEquals;

	public class EntityDecoratorTests
	{
		[Rule]
		public var byteCodeRule:ByteCodeRule = new ByteCodeRule();

		[Test]
		public function detectsIdOnFieldViaMetatag():void
		{
			var customer:Customer = new Customer(1);
			var decorator:EntityDecorator = new EntityDecorator(customer);
			assertEquals(1,decorator.entityKey);
		}
		[Test]
		public function detectsIdOnAccessor():void
		{
			var product:Product = new Product(1);
			var decorator:EntityDecorator = new EntityDecorator(product);
			assertEquals(1,decorator.entityKey);
		}
		
		[Test(expects="Error")]
		public function throwsErrorWithNoId():void
		{
			var book:Book = new Book(1);
			var decorator = new EntityDecorator(book);
		}
		[Test(expects="Error")]
		public function throwsErrorWithMultipleId():void
		{
			var invalid:InvalidWithMultipleIdFields = new InvalidWithMultipleIdFields();
			var decorator = new EntityDecorator(invalid);
		}
		
	}
}