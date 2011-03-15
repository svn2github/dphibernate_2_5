package org.dphibernate.loader
{
	import mx.rpc.AsyncToken;
	
	import org.as3commons.reflect.Type;
	import org.as3commons.reflect.TypeProviderKind;
	import org.dphibernate.ByteCodeRule;
	import org.dphibernate.EventChecker;
	import org.dphibernate.core.EntityStatus;
	import org.dphibernate.core.IEntity;
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.model.Book;
	import org.dphibernate.model.Order;
	import org.flexunit.assertThat;
	import org.flexunit.asserts.assertFalse;
	import org.flexunit.asserts.assertTrue;
	import org.mockito.integrations.any;
	import org.mockito.integrations.flexunit4.MockitoRule;
	import org.mockito.integrations.given;
	import org.mockito.integrations.verify;
	import org.dphibernate.loader.service.ILazyLoadService;

	public class LazyLoaderTests
	{
		private var loader:LazyLoader;
		
		[Rule]
		public var mockitoRule:MockitoRule = new MockitoRule();
		
		[Mock]
		public var service:ILazyLoadService;
		
		[Mock]
		public var entityResolver:IEntityManager
		
		private var book:Book;
		[Before]
		public function setup():void
		{
			Type.typeProviderKind = TypeProviderKind.XML;
			book = new Book(1);
			given(service.loadProxy(any(),any())).willReturn(new AsyncToken());
			loader = new LazyLoader(book as IEntity,service,entityResolver);
		}
		[Test]
		public function whenCallingLoad_that_serviceIsInvoked():void
		{
			loader.load();
			verify().that(service.loadProxy(1,"remote.Book"));
		}
		[Test]
		public function whenLoading_that_statusIsUpdated():void
		{
			loader.load();
			assertTrue(book.entityStatus & EntityStatus.LOADING);
			loader.result({});
			assertFalse(book.entityStatus & EntityStatus.LOADING);
			
			loader.load();
			assertTrue(book.entityStatus & EntityStatus.LOADING);
			loader.fault({});
			assertFalse(book.entityStatus & EntityStatus.LOADING);
			
		}
		
		[Test]
		public function whenLoading_that_eventsAreDispatched():void
		{
			var checker:EventChecker = new EventChecker(book);
			checker.expect(LazyLoadEvent.LOADING_START);
			loader.load();
			checker.assert();
			
			checker = new EventChecker(book);
			checker.expect(LazyLoadEvent.LOADING_COMPLETE);
			loader.result({});
			checker.assert();
			
			checker = new EventChecker(book);
			checker.expect(LazyLoadEvent.LOADING_FAILED);
			loader.fault({});
			checker.assert();
		}
			
	}
}