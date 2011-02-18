package org.dphibernate.rpc
{
	import mx.core.mx_internal;
	import mx.rpc.AbstractOperation;
	import mx.rpc.AsyncRequest;
	
	import net.digitalprimates.persistence.state.testObjects.Book;
	
	import org.flexunit.asserts.assertNotNull;
	import org.flexunit.asserts.assertNull;
	import org.mockito.integrations.any;
	import org.mockito.integrations.never;
	import org.mockito.integrations.verify;
	
	use namespace mx_internal;
	
	[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
	public class LoadProxyRequestBufferTests
	{
		private var remoteObject:HibernateRemoteObject;
		
		[Mock]
		public var stubbedAsyncRequest:AsyncRequest;
		
		[Before]
		public function setup():void
		{
			remoteObject = new HibernateRemoteObject("",new LoadDPProxyOperationBufferFactory());
			remoteObject.operationPostConstructDecorator = stubOperation;
		}
		
		[Test]
		public function createsBufferForLoadDpProxyOperation():void
		{
			var operation:AbstractOperation = remoteObject.getOperation("loadDPProxy");
			assertNotNull(operation.operationManager);
		}
		[Test]
		public function doesNotCreateBufferForOtherOperations():void
		{
			var operation:AbstractOperation = remoteObject.getOperation("someOtherOperation");
			assertNull(operation.operationManager);
		}
		[Test]
		public function whenNoBufferPresentRemoteCallIsSent():void
		{
			remoteObject.loadProxy("123",new Book());
			remoteObject.operationBufferFactory = null; // Disable the buffer, so the call is sent
			verifyRemoteCallDispatched();
		}
		[Test]
		public function whenBufferingRequestRemoteCallIsNotSent():void
		{
			remoteObject.loadProxy("123",new Book());
			verifyRemoteCallNotDispatched();
		}
		/**
		 * Rewires an operation to prevent it from actually
		 * trying to communicate over the wire.
		 * Instead, it's wired to stubbedAsyncRequest,
		 * which stubs out the actual communication */		
		private function stubOperation(op:AbstractOperation):void
		{
			op.asyncRequest= stubbedAsyncRequest;
		}
		private function verifyRemoteCallDispatched():void
		{
			verify().that(stubbedAsyncRequest.invoke(any(),any()));
		}
		private function verifyRemoteCallNotDispatched():void
		{
			verify(never()).that(stubbedAsyncRequest.invoke(any(),any()));
		}
	}
}