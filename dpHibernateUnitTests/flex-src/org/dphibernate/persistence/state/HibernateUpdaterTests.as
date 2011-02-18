package org.dphibernate.persistence.state
{
	import flexunit.framework.Assert;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	
	import net.digitalprimates.persistence.state.BaseTestCase;
	import net.digitalprimates.persistence.state.testObjects.Author;
	import net.digitalprimates.persistence.state.testObjects.Book;
	
	import org.dphibernate.persistence.state.ChangeMessageGenerator;
	import org.dphibernate.persistence.state.HibernateUpdater;
	import org.dphibernate.persistence.state.ObjectChangeMessage;
	import org.dphibernate.persistence.state.ObjectChangeResult;
	import org.dphibernate.persistence.state.StateRepository;
	import org.mockito.integrations.any;
	import org.mockito.integrations.given;

	public class HibernateUpdaterTests extends BaseTestCase
	{
		public function HibernateUpdaterTests()
		{
		}
		private var changeMessageGenerator:ChangeMessageGenerator;

		[Before]
		public override function setUp():void
		{
			changeMessageGenerator = new ChangeMessageGenerator();
		}

		[Test]
		public function hasChangesFalseAfterUpdate():void
		{
			var author:Author=getTestAuthor(mockService);
			var returnToken:AsyncToken=new AsyncToken(null);
			given(mockService.saveProxy(any(),any())).willReturn(returnToken);

			StateRepository.store(author);
			author.name="Sondehim";
			Assert.assertTrue(changeMessageGenerator.hasChanges(author));
			var token:AsyncToken=author.save(); // Should be same as returnToken

			HibernateUpdater.saveCompleted(ResultEvent.createEvent(null, token));
			Assert.assertFalse(changeMessageGenerator.hasChanges(author));
		}

		[Test]
		public function givenUpdatingNewObjectCompletedThatNewIdIsSetOnObject():void
		{
			var author:Author=getTestAuthor(mockService);
			StateRepository.store(author);
			var book:Book=getNewBook("Getting Real", author);
			author.books.addItem(book);
			
			var bookChanges : ObjectChangeMessage =  changeMessageGenerator.getChangesForEntityOnly( book );
			Assert.assertTrue( bookChanges.isNew );
			Assert.assertTrue( bookChanges.hasChanges );
			
			var returnToken:AsyncToken=new AsyncToken(null);
			given(mockService.saveProxy(any(),any())).willReturn(returnToken);
			
			author.save();
			
			// Update result returned from server:
			var updateResult : ObjectChangeResult = ObjectChangeResult.create( Book , book.proxyKey , 2 );
			
			HibernateUpdater.saveCompleted( ResultEvent.createEvent( new ArrayCollection([ updateResult ]) , returnToken ) );
			
			bookChanges = changeMessageGenerator.getChangesForEntityOnly( book );
			Assert.assertFalse( bookChanges.isNew );
			Assert.assertFalse( bookChanges.hasChanges );
			
			Assert.assertEquals( 2 , book.proxyKey );
		}

		[Test]
		public function givenChangesMadeToAnObjectWhileUpdateInProgressThatChangesAreAppliedAfterUpdateCompletes():void
		{
			var author:Author=getTestAuthor(mockService);
			StateRepository.store(author);
			author.name = "Schwartz";
			author.publisher.name = "Manning";
				
			var returnToken:AsyncToken=new AsyncToken(null);
			given(mockService.saveProxy(any(),any())).willReturn(returnToken);
			
			author.save();
			
//			Assert.assertTrue( StateRepository.hasPendingSave( author ) );
//			Assert.assertTrue( StateRepository.hasPendingSave( author.publisher ) );
			// Save is in progress.  Any changes should be queued
			author.age = 23;
			author.name = "Sondheim";
			
			HibernateUpdater.saveCompleted(ResultEvent.createEvent(null, returnToken));
			
			var authorChanges : ObjectChangeMessage = changeMessageGenerator.getChangesForEntityOnly( author );
			Assert.assertTrue( authorChanges.hasChanges );
			Assert.assertTrue( authorChanges.hasChangedProperty( "age" ) );
			Assert.assertTrue( authorChanges.hasChangedProperty( "name" ) );
		}
		
		[Test]
		public function afterSuccessfulCreationSubsequentUpdatesSentWithCorrectKey() : void
		{
			var author:Author=getTestAuthor(mockService);
			StateRepository.store(author);
			var oldId : int = 4;
			var newId : int = 100;
			var book:Book=getBook(oldId, "Getting Real", author);
			author.books.addItem(book);
			
			var returnToken:AsyncToken=new AsyncToken(null);
			given(mockService.saveProxy(any(),any())).willReturn(returnToken);
			
			author.save();
			
			// Update result returned from server:
			var updateResult : ObjectChangeResult = ObjectChangeResult.create( Book , oldId , newId );
			
			HibernateUpdater.saveCompleted( ResultEvent.createEvent( new ArrayCollection([ updateResult ]) , returnToken ) );
			
			book.title = "Getting a little bit more real";
			
			var bookChanges : ObjectChangeMessage = changeMessageGenerator.getChangesForEntityOnly( book );
			Assert.assertNotNull( bookChanges );
			Assert.assertEquals( newId , bookChanges.owner.proxyId );
		}
		
		
	}
}