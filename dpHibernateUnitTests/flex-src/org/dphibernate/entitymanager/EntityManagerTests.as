package org.dphibernate.entitymanager
{
	import net.digitalprimates.persistence.state.BaseTestCase;
	import net.digitalprimates.persistence.state.testObjects.Author;
	import net.digitalprimates.persistence.state.testObjects.Book;
	import net.digitalprimates.persistence.state.testObjects.Publisher;
	
	import org.flexunit.asserts.assertFalse;
	import org.flexunit.asserts.assertTrue;
	import org.mockito.integrations.given;
	import org.mockito.integrations.mock;
	
	public class EntityManagerTests extends BaseTestCase
	{
		[Mock]
		public var mockEntityManager:IEntityManager;

		[Before]
		override public function setUp():void
		{
			super.setUp();
			mockEntityManager = mock(IEntityManager);
		}
		[Test]
		public function containsReturnsTrueUsingDifferentObjectReference():void
		{
			var entityManager:EntityManager = new EntityManager();
			var book:Book = getBook(1,"Effective Java",getTestAuthor());
			entityManager.putEntity(book);
			
			// get another reference to book
			var sameBook:Book = getBook(1,"Effective Java",getTestAuthor());
			assertFalse(book == sameBook);

			assertTrue(entityManager.containsEntity(sameBook));
		}
		[Test]
		public function containsReturnsTrueUsingSameObjectReference():void
		{
			var entityManager:EntityManager = new EntityManager();
			var book:Book = getBook(1,"Effective Java",getTestAuthor());
			entityManager.putEntity(book);
			assertTrue(entityManager.containsEntity(book));
		}
		
		[Test]
		public function findReturnsEntity():void
		{
			var entityManager:EntityManager = new EntityManager();
			var book:Book = getBook(1,"Effective Java",getTestAuthor());
			entityManager.putEntity(book);
			assertTrue(entityManager.containsEntity(book));
		}
		[Test]
		public function testWhenEntityIsPresentInStoreThatRemoteLoadIsNotTriggered():void
		{
		}
		[Test]
		public function testWhenEntityIsPresentInStoreThatGetterOnUninitalizedPropertyReturnsObject():void
		{
		}
		[Test]
		public function whenEntityIsPresentInStoreThatCallingGetterOnUninitializedProxyDoesNotMakeProxyInitialized():void
		{
			
		}
	}
}