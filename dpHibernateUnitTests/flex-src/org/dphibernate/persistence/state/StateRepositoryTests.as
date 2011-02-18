package org.dphibernate.persistence.state
{
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import flexunit.framework.Assert;
	
	import mx.collections.ArrayCollection;
	
	import net.digitalprimates.persistence.state.testObjects.Author;
	import net.digitalprimates.persistence.state.testObjects.Book;
	
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.util.ClassUtils;
	import org.mockito.integrations.any;
	import org.mockito.integrations.mock;
	import org.mockito.integrations.never;
	import org.mockito.integrations.verify;
	import net.digitalprimates.persistence.state.BaseTestCase;
	
	[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
	public class StateRepositoryTests extends BaseTestCase
	{
		
		public function StateRepositoryTests()
		{
		}
		
		private var changeMessageGenerator : ChangeMessageGenerator;
		[Before]
		public override function setUp():void
		{
			super.setUp();
			changeMessageGenerator = new ChangeMessageGenerator();
		}
		[Test]
		public function testCreatingObject() : void
		{
			var author : Author = new Author();
			author.name = "Sondheim";
			var changes : Array = changeMessageGenerator.getChanges( author );
			Assert.assertEquals( 1 , changes.length );
			Assert.assertTrue( changes[0] is ObjectChangeMessage );
			var changeMessage : ObjectChangeMessage = changes[0] as ObjectChangeMessage;
			Assert.assertTrue( changeMessage.isNew );
			Assert.assertEquals( 2 , changeMessage.changedProperties.length );
			
			// Because age is a primitive, it will always turn up...			
			assertContainsPropertyChange( changeMessage , "age" , null , 0 );
			assertContainsPropertyChange( changeMessage , "name" , null , "Sondheim" );
		}
		
		[Test]
		public function testAssigningPropertyToNewObject() : void
		{
			var newAuthor : Author = new Author();
			newAuthor.name = "Sondheim";
			
			var author : Author = getTestAuthor();
			var book : Book = new Book();
			book.title = "Effective Java";
			book.author = author;
			StateRepository.store( book );
			
			book.author = newAuthor;			
			var changes : Array = changeMessageGenerator.getChanges( book );

			Assert.assertEquals( 2 , changes.length );
			assertContainsObjectChangeMessage( changes , book );
			assertContainsObjectChangeMessage( changes , newAuthor );
			
			var bookChangeMessage : ObjectChangeMessage = findChangeMessageForObject( changes , Book , book.proxyKey );
			Assert.assertEquals( 1 , bookChangeMessage.changedProperties.length );
			Assert.assertEquals( "author" , bookChangeMessage.changedProperties[0].propertyName );
		}
		[Test]
		public function testStore():void
		{
			var author:Author=getTestAuthor();
			var typeName:String=getQualifiedClassName(author);
			Assert.assertFalse(StateRepository.contains(author));
			StateRepository.store(author);
			Assert.assertTrue(StateRepository.contains(author));
		}

		[Test]
		public function testGeneratesKey():void
		{
			var author:Author=getTestAuthor();
			var key:String=StateRepository.getKey(author);
			var expected:String="net.digitalprimates.persistence.hibernate.testObjects.Author::123";
			Assert.assertEquals(expected, key);
		}

		[Test]
		public function detectsChangeOfSimpleProperty():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.name="Sondheim";
			var changes:Array = changeMessageGenerator.getChanges(author);
			Assert.assertNotNull(changes);
			Assert.assertEquals( 1 , changes.length );
			var changeMessage : ObjectChangeMessage = changes[0] as ObjectChangeMessage;
			Assert.assertEquals(1, changeMessage.changedProperties.length);
			var record:PropertyChangeMessage=changeMessage.getChangeAt(0);
			Assert.assertEquals("name", record.propertyName);
			Assert.assertEquals("Bloch", record.oldValue);
			Assert.assertEquals("Sondheim", record.newValue);
		}

		[Test]
		public function testHasChangedProperty():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			Assert.assertFalse(StateRepository.hasChangedProperty(author, "name"));
			author.name="Sondheim";
			Assert.assertTrue(StateRepository.hasChangedProperty(author, "name"));
		}

		[Test]
		public function givenTwoChangesToTheSamePropertyThatOnlyTheLatestIsStored():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.name="Sondhiem";
			author.name="Schwartz";
			var changes:ObjectChangeMessage=changeMessageGenerator.getChangesForEntityOnly(author);
			Assert.assertNotNull(changes);
			Assert.assertEquals(1, changes.numChanges);
			var record:PropertyChangeMessage=changes.getChangeAt(0);
			Assert.assertEquals("name", record.propertyName);
			Assert.assertEquals("Bloch", record.oldValue);
			Assert.assertEquals("Schwartz", record.newValue);
		}

		[Test]
		public function testChangesToCollectionMember():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			var book:Book=author.books.getItemAt(0) as Book;
			book.title="Hibernate in Action";
			var changes:ObjectChangeMessage=changeMessageGenerator.getChangesForEntityOnly(book);
			Assert.assertEquals(1, changes.numChanges);
		}

		[Test]
		public function testChangesToNestedProperty():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.publisher.name="Manning";
			Assert.assertTrue( changeMessageGenerator.hasChanges( author ) );
			Assert.assertTrue( changeMessageGenerator.hasChanges( author.publisher ) );
			var authorChanges : Array = changeMessageGenerator.getChanges( author);
			
			var changes:ObjectChangeMessage=changeMessageGenerator.getChangesForEntityOnly(author.publisher);
			Assert.assertEquals(1, changes.numChanges);
			Assert.assertTrue(changes.hasChangedProperty("name"));
			Assert.assertEquals("net.digitalprimates.persistence.hibernate.testObjects.Publisher", changes.owner.remoteClassName);
		}
		
		public function addingToCollectionGeneratesFullObjectGraph():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			var book:Book=getNewBook("Getting Real", author);
			author.books.addItem(book);

			var changes:ObjectChangeMessage=changeMessageGenerator.getChangesForEntityOnly(book);
			Assert.assertNotNull(changes);
			Assert.assertTrue(changes.isNew);
			Assert.assertTrue(changes.hasChangedProperty("title"));
			var titleChange:PropertyChangeMessage=changes.getPropertyChange("title");
			Assert.assertNotNull(titleChange);
			Assert.assertNull(titleChange.oldValue);
			Assert.assertEquals("Getting Real", titleChange.newValue);

			Assert.assertTrue(changes.hasChangedProperty("author"));
			var authorChange:PropertyChangeMessage=changes.getPropertyChange("author");
			Assert.assertNotNull(authorChange);
			Assert.assertNull(authorChange.oldValue);
			Assert.assertTrue(authorChange.newValue is HibernateProxyDescriptor);
			Assert.assertEquals(123, authorChange.newValue.proxyId);
			Assert.assertEquals("net.digitalprimates.persistence.hibernate.testObjects.Author", authorChange.newValue.remoteClassName);
		}

		[Test]
		public function ignoreChangesToTransientProperties():void
		{
			var author:Author=getTestAuthor();
			var book:Book=getBook(4, "Getting Real", author);
			book.pages=100;
			StateRepository.store(book);
			Assert.assertFalse(changeMessageGenerator.hasChanges(book));
			book.pages=123;
			Assert.assertFalse(changeMessageGenerator.hasChanges(book));
		}

		[Test]
		public function collectionStateIncludedWhenChangesMadeToCollection():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			var book:Book=getBook(4, "Getting Real", author);
			author.books.addItem(book);
			var changes:ObjectChangeMessage=changeMessageGenerator.getChangesForEntityOnly(author);
			Assert.assertTrue(changes.hasChangedProperty("books"));
			var bookChanges:CollectionChangeMessage=changes.getPropertyChange("books") as CollectionChangeMessage;
			Assert.assertNotNull(bookChanges);
			Assert.assertEquals(3, bookChanges.newValue.length);

			Assert.assertNotNull(findChangeMessageForObject(bookChanges.newValue as Array, Book, 1));
			Assert.assertNotNull(findChangeMessageForObject(bookChanges.newValue as Array, Book, 2));
			Assert.assertNotNull(findChangeMessageForObject(bookChanges.newValue as Array, Book, 4));
		}

		[Test]
		public function collectionStateIncludedWhenObjectRemovedFromCollection():void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.books.removeItemAt(0);
			var changes:ObjectChangeMessage=changeMessageGenerator.getChangesForEntityOnly(author);
			Assert.assertTrue(changes.hasChangedProperty("books"));
			var bookChanges:CollectionChangeMessage=changes.getPropertyChange("books") as CollectionChangeMessage;
			Assert.assertNotNull(bookChanges);
			Assert.assertEquals(1, bookChanges.newValue.length);
		}

		[Test]
		public function noLazyLoadTriggeredDuringStorage() : void
		{
			var author:Author=getTestAuthorProxy();
			StateRepository.store(author);
			verify(never()).that(mockService.loadProxy(any(),any()));
//			Assert.assertTrue( mockService.errorMessage() , mockService.success() );
		}
		
		[Test]
		public function testResetState() : void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.name = "Sondheim";
			var book : Book = author.books.getItemAt( 0 ) as Book;
			book.title = "Antoher";
			author.publisher.name = "Publisher2"
			Assert.assertTrue( new ChangeMessageGenerator().hasChanges( author ) );
			Assert.assertTrue( new ChangeMessageGenerator().hasChanges( book ) );
			Assert.assertTrue( new ChangeMessageGenerator().hasChanges( author.publisher ) );
			
			StateRepository.removeFromStore( author );
			
			Assert.assertFalse( new ChangeMessageGenerator().hasChanges( author ) );
			Assert.assertFalse( new ChangeMessageGenerator().hasChanges( book ) );
			Assert.assertFalse( new ChangeMessageGenerator().hasChanges( author.publisher ) );
			
		}
		[Test]
		public function testCollectionChangesCapturedAfterCollectionPropertyChanged() : void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.books = new ArrayCollection();
			var book : Book = getBook(4 , "Getting Real" , author);
			Assert.assertFalse( StateRepository.contains( book ) );
			author.books.addItem( book );
			Assert.assertTrue( StateRepository.contains( book ) );
			
		}
		
		[Test]
		public function whenValueIsChangedBackToOriginalValueNoChangeMessageIsStored() : void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			author.name = "Schwartz";
			Assert.assertTrue( changeMessageGenerator.hasChanges( author ) );
			author.name = "Bloch"
			Assert.assertFalse( changeMessageGenerator.hasChanges( author ) );
		}
		
		[Test]
		[Ignore("Currently don't store original value of a collection, so can't test this")]
		public function whenCollectionIsChangedBackToOriginalValueNoChangeMessageIsStored() : void
		{
			var author:Author=getTestAuthor();
			StateRepository.store(author);
			var book : Book = author.books.removeItemAt( 1 ) as Book;
			Assert.assertTrue( changeMessageGenerator.hasChanges( author ) );
			author.books.addItem( book );
			Assert.assertFalse( changeMessageGenerator.hasChanges( author ) );
		}
		
		
		//==========================================================
		// Helper methods
		//==========================================================
		private function findChangeMessageForObject(changeMessages:Array, clazz:Class, id:Object):ObjectChangeMessage
		{
			var remoteClassName:String=ClassUtils.getRemoteClassName(clazz);
			for each (var changeMessage:ObjectChangeMessage in changeMessages)
			{
				if (changeMessage.owner.remoteClassName == remoteClassName && changeMessage.owner.proxyId == id)
				{
					return changeMessage;
				}
			}
			return null;
		}
		private function assertContainsPropertyChange( message : ObjectChangeMessage , propertyName : String , oldValue : Object , newValue : Object ) :void
		{
			for each ( var propertyChange : PropertyChangeMessage in message.changedProperties )
			{
				if ( propertyChange.propertyName == propertyName )
				{
					Assert.assertEquals( oldValue , propertyChange.oldValue );
					Assert.assertEquals( newValue , propertyChange.newValue );
					return;
				}
			}
			Assert.fail("Property change message not found in Object change message for property " + propertyName );
		} 
		private function assertContainsObjectChangeMessage( changeMessages : Array , object : IHibernateProxy ) : void
		{
			var clazz : Class = getDefinitionByName( getQualifiedClassName( object ) ) as Class;
			var message : ObjectChangeMessage = findChangeMessageForObject( changeMessages , clazz , object.proxyKey );
			Assert.assertNotNull( message ); 
		}
	}
}