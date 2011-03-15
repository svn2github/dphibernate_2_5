package org.dphibernate.loader
{
	import org.dphibernate.core.EntityStatus;
	import org.dphibernate.core.IEntity;
	import org.dphibernate.model.Author;
	import org.flexunit.assertThat;
	import org.flexunit.asserts.assertNull;
	import org.hamcrest.object.equalTo;
	import org.mockito.integrations.any;
	import org.mockito.integrations.flexunit4.MockitoClassRunner;
	import org.mockito.integrations.flexunit4.MockitoRule;
	import org.mockito.integrations.given;
	import org.mockito.integrations.never;
	import org.mockito.integrations.verify;

	[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
	public class PropertyInterceptorTests
	{
		var runner:MockitoClassRunner;

		[Mock]
		public var loaderFactory:ILazyLoaderFactory;
		
		[Mock]
		public var loader:ILazyLoader;

		private var interceptor:PropertyInterceptor;
		[Before]
		public function setup():void
		{
			interceptor = new PropertyInterceptor(loaderFactory);
			given(loaderFactory.newInstance(any(),any())).willReturn(loader);
		}
		[Test]
		public function givenPropertyIsNotSet_that_lazyLoadIsTriggered():void
		{
			// Note - I'd rather test with a mock of entity, but neither mockolate nor mockito seem to support 
			// dynamic methods
			var author:Author = new Author(1);
			author.entityInitialized = false;
			
			interceptor.getProperty(author,"name");
			
			verify().that(loader.load());
		}
		[Test]
		public function givenEntityIsInitialzed_and_propertyReturnsNull_that_lazyLoadIsNotTriggered():void
		{
			// Note - I'd rather test with a mock of entity, but neither mockolate nor mockito seem to support 
			// dynamic methods
			var author:Author = new Author(1);
			author.name = null;
			author.entityInitialized = true;
			
			var result:* = interceptor.getProperty(author,"name");
			
			verify(never()).that(loader.load());
			assertNull(result);
		}
		[Test]
		public function givenPropertyIsSet_that_lazyLoadIsNotTriggered():void
		{
			var author:Author = new Author(1);
			author.entityInitialized = true;
			author.name = "Arthur";
			
			var result:* = interceptor.getProperty(author,"name");
			
			verify(never()).that(loader.load());
			assertThat(result,equalTo("Arthur"));
		}
		
		[Test]
		public function givenLoadAlreadyInProgress_that_newLoadIsNotTriggered():void
		{
			var author:Author = new Author(1);
			author.entityInitialized = false;
			author.entityStatus |= EntityStatus.LOADING;
			
			var result:* = interceptor.getProperty(author,"name");
			
			verify(never()).that(loader.load());
			assertNull(result);
		}
	}
}