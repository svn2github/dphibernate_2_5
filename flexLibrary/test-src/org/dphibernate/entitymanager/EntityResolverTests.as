package org.dphibernate.entitymanager
{
	import mx.collections.ArrayCollection;
	import mx.core.IUID;
	
	import org.dphibernate.helpers.delegateAnswerTo;
	import org.dphibernate.model.Author;
	import org.dphibernate.model.Book;
	import org.flexunit.asserts.assertFalse;
	import org.flexunit.asserts.assertTrue;
	import org.hamcrest.assertThat;
	import org.hamcrest.collection.hasItems;
	import org.hamcrest.core.not;
	import org.hamcrest.object.equalTo;
	import org.hamcrest.object.sameInstance;
	import org.mockito.integrations.any;
	import org.mockito.integrations.eq;
	import org.mockito.integrations.flexunit4.MockitoClassRunner;
	import org.mockito.integrations.given;
	import org.mockito.integrations.mock;
	import org.mockito.integrations.times;
	import org.mockito.integrations.verify;

	[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
	public class EntityResolverTests
	{
		private var runner:MockitoClassRunner;
		private var entityResolver:EntityManager;
		
		[Mock]
		public var resolutionPolicy:IResolutionPolicy;
		
		[Before]
		public function setup():void
		{
			resolutionPolicy = mock(IResolutionPolicy);
			entityResolver = new EntityManager(resolutionPolicy);
		}
		
		[Test]
		public function givenEntityDoesntExist_that_entityIsStored():void
		{
			var book:Book = new Book(1);
			assertFalse(entityResolver.contains(book));
			
			entityResolver.resolve(book);
			assertTrue(entityResolver.contains(book));
		}

		[Test]
		public function givenStoredEntity_that_retrieveReturnsSameInstance():void
		{
			var book:Book = new Book(1);
			entityResolver.store(book);
			
			var retrieved:Book = entityResolver.findMatchingEntity(book);
			assertThat(book,sameInstance(retrieved));
		}
		[Test]
		public function givenEntityDoesntExist_that_entityIsReturnedUnchanged():void
		{
			given(resolutionPolicy.getResolvedEntity(any(),any(),any())).will(delegateAnswerTo(returnOld));
			var parent:Book = new Book(1);
			var resolved:Book = entityResolver.resolve(parent);
			assertThat(parent,sameInstance(resolved));
		}
		[Test]
		public function givenEntityExists_that_resolutionPolicyIsCalled():void
		{
			given(resolutionPolicy.getResolvedEntity(any(),any(),any())).will(delegateAnswerTo(returnOld));
			var book:Book = new Book(1);
			entityResolver.resolve(book);
			
			var book2:Book= new Book(1);
			entityResolver.resolve(book2);
			
			verify(times(1)).that(resolutionPolicy.getResolvedEntity(eq(book),eq(book2),any()));
		}
		
		[Test]
		public function givenCollectionOfEntities_that_collectionIsResolved():void
		{
			var collection:ArrayCollection = new ArrayCollection([new Book(1),new Book(2)]);
			entityResolver.resolveCollection(collection);
			
			assertTrue(entityResolver.contains(new Book(1)));
			assertTrue(entityResolver.contains(new Book(2)));
		}

		//================================================
		// Integration tests (ie., don't use mocks)
		//================================================
		[Test]
		public function whenMergingManagedChildProperty_that_propertiesAreUpdated():void
		{
			entityResolver = new EntityManager(new NewestIsBestResolutionPolicy());
			
			var newBook:Book = new Book(1);
			newBook.author = new Author(1);
			newBook.author.name = "Tom";

			var oldBook:Book = new Book(1);
			var oldAuthor:Author = new Author(1);
			oldBook.author = oldAuthor;
			entityResolver.store(oldBook);
			
			var resolvedEntity:Book = entityResolver.resolve(newBook);
			assertThat(resolvedEntity,sameInstance(oldBook));
			assertThat(resolvedEntity.author.name,equalTo("Tom"));
		}
		
		[Test]
		public function givenTwoParentsWithCommonChildren_That_ChildrenOnSecondParentAreResolved():void
		{
			entityResolver = new EntityManager(new NewestIsBestResolutionPolicy());
			
			var authorA:Author = Author.createWithBooks(1,[new Book(1),new Book(2)]);
			// Note that parentB has children which are different instances, but match by identity.
			var authorB:Author = Author.createWithBooks(2,[new Book(1),new Book(2)]);

			entityResolver.resolve(authorA);
			entityResolver.resolve(authorB);
			
			assertThat(authorA,not(sameInstance(authorB)));
			assertThat(authorA.books,hasItems(
					sameInstance(authorB.getBookAt(0)),
					sameInstance(authorB.getBookAt(1))
			));
			
		}
		
		[Test]
		public function resolvingParentAlsoStoresChildren():void
		{
			entityResolver = new EntityManager(new NewestIsBestResolutionPolicy());
			var author:Author = Author.createWithBooks(1,[new Book(1),new Book(2)]);
			entityResolver.resolve(author);
			
			assertTrue(entityResolver.contains(new Book(1)));
			
		}
		// Helpers
		/**
		 * Helper method for mocking entity resolution -- returns oldVersion
		 * */
		private function returnOld(oldVersion:IUID,newVersion:IUID, entityResolver:EntityManager):IUID
		{
			return oldVersion;
		}
	}
}
