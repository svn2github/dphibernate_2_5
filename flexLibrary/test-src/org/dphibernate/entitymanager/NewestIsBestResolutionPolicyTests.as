package org.dphibernate.entitymanager
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.ListCollectionView;
	
	import org.dphibernate.helpers.delegateAnswerTo;
	import org.dphibernate.model.Author;
	import org.dphibernate.model.Book;
	import org.flexunit.asserts.assertFalse;
	import org.flexunit.asserts.assertTrue;
	import org.hamcrest.assertThat;
	import org.hamcrest.collection.arrayWithSize;
	import org.hamcrest.collection.hasItem;
	import org.hamcrest.collection.hasItems;
	import org.hamcrest.core.not;
	import org.hamcrest.object.equalTo;
	import org.hamcrest.object.hasPropertyWithValue;
	import org.hamcrest.object.nullValue;
	import org.hamcrest.object.sameInstance;
	import org.mockito.integrations.any;
	import org.mockito.integrations.eq;
	import org.mockito.integrations.flexunit4.MockitoClassRunner;
	import org.mockito.integrations.given;
	import org.mockito.integrations.mock;
	import org.mockito.integrations.times;
	import org.mockito.integrations.verify;
	import org.spicefactory.lib.reflect.ClassInfo;

	[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
	public class NewestIsBestResolutionPolicyTests
	{
		private var runner:MockitoClassRunner
		
		private var policy:NewestIsBestResolutionPolicy;
		
		[Mock]
		public var entityResolver:EntityManager;
		[Before]
		public function setup():void
		{
			policy = new NewestIsBestResolutionPolicy();
			entityResolver = mock(EntityManager);
		}
		[Test]
		public function testCollectionDetection():void
		{
			assertTrue(policy.isCollection(ClassInfo.forClass(ArrayCollection)));
			assertTrue(policy.isCollection(ClassInfo.forClass(ArrayList)));
			assertTrue(policy.isCollection(ClassInfo.forClass(ListCollectionView)));
			assertFalse(policy.isCollection(ClassInfo.forClass(Object)));
			assertFalse(policy.isCollection(ClassInfo.forClass(Dictionary)));
			
			assertTrue(policy.isArray(ClassInfo.forClass(Array)));
		}
		[Test]
		public function testCorrectPropertiesToMergeAreDetected():void
		{
			var book:Book = new Book(1);
			var properties:Array = policy.getProperties(book);
			
			assertThat(properties,hasItem(hasPropertyWithValue("name","author")));
			assertThat(properties,hasItem(hasPropertyWithValue("name","title")));
			assertThat(properties,not(hasItem(hasPropertyWithValue("name","amazonRating"))));
			assertThat(properties,not(hasItem(hasPropertyWithValue("name","pages"))));
		}
		[Test]
		public function propertiesFromNew_mergedToOld():void
		{
			var newBook:Book = new Book(1);
			newBook.title = "Hello";
			var oldBook:Book = new Book(1);

			var resolved:Book = policy.getResolvedEntity(oldBook,newBook,entityResolver);
			assertThat(resolved,sameInstance(oldBook));
			assertThat(resolved.title,equalTo("Hello"));
		}
		
		/**
		 * Asserts the assumption that the value we received from the server (new value)
		 * may not be a fully populated object graph.
		 * 
		 * Therefore, if our old value contains a value, and the new value does not,
		 * the old value should not be changed */ 
		[Test]
		public function givenOldHasValue_andNewHasNull_and_copyNullSetToFalse_that_oldValueRetained():void
		{
			// Default.  Shown here only for clarity
			policy.copyNullValuesFromNewToOld = false;
			
			var newBook:Book = new Book(1)
			var oldBook:Book = new Book(1)
			// oldBook.name is populated, newBook.name is null
			oldBook.title = "Hello";
			
			var resolved:Book = policy.getResolvedEntity(oldBook,newBook,entityResolver);
			assertThat(resolved,sameInstance(oldBook));
			assertThat(resolved.title,equalTo("Hello"));
		}
		/**
		 * Asserts the assumption that the value we received from the server (new value)
		 * may not be a fully populated object graph.
		 * 
		 * Therefore, if our old value contains a value, and the new value does not,
		 * the old value should not be changed */ 
		[Test]
		public function givenOldHasValue_andNewHasNull_and_copyNullSetToTrue_that_oldValueSetToNull():void
		{
			policy.copyNullValuesFromNewToOld = true;
			
			var newBook:Book = new Book(1)
			var oldBook:Book = new Book(1)
			// oldBook.name is populated, newBook.name is null
			oldBook.title = "Hello";
			
			var resolved:Book = policy.getResolvedEntity(oldBook,newBook,entityResolver);
			assertThat(resolved,sameInstance(oldBook));
			assertThat(resolved.title,nullValue());
		}
		
		/**
		 * When we find a value that is an entity itself,
		 * rather than attempting to simply merge the value,
		 * defer back to the entityResolver to handle it.
		 * 
		 * Note - often, the entityResolver will simply re-invoke a Policy to
		 * handle that specific entity.  However, it allows the entityResolver
		 * to map versions of children back to their originally stored values
		 * */
		[Test]
		public function givenValueIsEntity_that_entityResolverIsInvoked():void
		{
			var newBook:Book = new Book(1)
			var author:Author= new Author(1);
			newBook.author = author;
			
			var oldBook:Book = new Book(1)
			
			policy.getResolvedEntity(oldBook,newBook,entityResolver);
			
			// The entity resolver should be asked to resolve the Author property
			verify().that(entityResolver.resolve(eq(author),any()));
		}
		[Test]
		public function somethingAboutCollections():void
		{
			var oldAuthor:Author = Author.createWithBooks(1,[Book.createWithTitle(1,"Book1")]);
			var newAuthor:Author = Author.createWithBooks(1,[Book.createWithTitle(2,"Book2"),Book.createWithTitle(3,"Book3")]);
			
			given(entityResolver.resolve(any(),any())).will(delegateAnswerTo(returnEntity));
			policy.getResolvedEntity(oldAuthor,newAuthor,entityResolver);
			
			// Note that entityResolver should've been invoked for each entity in the newBook collection
			verify(times(2)).that(entityResolver.resolve(any(),any()));
			
			assertThat(oldAuthor.books,arrayWithSize(2));
			assertThat(oldAuthor.books,hasItem(newAuthor.getBookAt(0)));
			assertThat(oldAuthor.books,hasItem(newAuthor.getBookAt(1)));
		}
		
		[Test]
		public function oldValueIsNull_and_newValueIsCollection():void
		{
			var oldAuthor:Author = new Author(1);
			oldAuthor.books = null;
			
			var newAuthor:Author = Author.createWithBooks(1,[Book.createWithTitle(1,"Book1"),Book.createWithTitle(2,"Book2")]);
			policy.getResolvedEntity(oldAuthor,newAuthor,entityResolver);
			
			assertThat(oldAuthor.books.length,equalTo(2));
			assertThat(oldAuthor.books,hasItems(newAuthor.getBookAt(0),newAuthor.getBookAt(1)));
		}
		[Test]
		public function oldAndNewValuesAreBothCollectionsOfSimpleObjects():void
		{
			
			var oldChapters:Array = ["Chapter1","Chapter2"];
			var oldBook:Book = Book.createWithChapters(1,oldChapters);
			var newBook:Book = Book.createWithChapters(1,["Chapter3","Chapter4"]);
			policy.getResolvedEntity(oldBook,newBook,entityResolver);
			
			assertThat(oldBook.chapters,arrayWithSize(2));
			assertThat(oldBook.chapters,hasItems("Chapter3","Chapter4"));
			// Note - only the contents of the array should be changed, not the array itself
			assertThat(oldBook.chapters,sameInstance(oldChapters)); 
		}
		[Test]
		public function oldAndNewValuesAreBothCollectionsOfEntities():void
		{
			given(entityResolver.resolve(any(),any())).will(delegateAnswerTo(returnEntity));
			
			var oldBooks:ArrayCollection = new ArrayCollection([new Book(1),new Book(2)]);
			var oldAuthor:Author = new Author(1);
			oldAuthor.books = oldBooks;
			var newAuthor:Author = Author.createWithBooks(1,[new Book(3),new Book(4)]);
			
			policy.getResolvedEntity(oldAuthor,newAuthor,entityResolver);
			
			assertThat(oldAuthor.books.length,equalTo(2));
			assertThat(oldAuthor.books,hasItems(newAuthor.getBookAt(0),newAuthor.getBookAt(1)));
			// Note - only the contents of the array should be changed, not the array itself
			assertThat(oldAuthor.books,sameInstance(oldBooks)); 
		}
		// Helper method for Mockito stubbing
		private function returnEntity(entity:*,...ignored):*
		{
			return entity;
		}
		
	}
}
