package org.dphibernate.rpc
{
	import com.hexagonstar.util.debug.StopWatch;
	
	import flash.events.TimerEvent;
	import flash.utils.Proxy;
	import flash.utils.Timer;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.rpc.AsyncToken;
	import mx.rpc.Responder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.dphibernate.util.LogUtil;

	use namespace mx_internal;
	/**
	 * A class which buffers requests for dpProxies.
	 * 
	 * When an initial call to load a proxy is made, the call is delayed
	 * by <param>initialDelayThreshold</param> (but no more than <param>maxDelayThreshold</param>
	 * to see if other calls are being requested.
	 * 
	 * Any additional calls to load dpHibernateProxies during this time frame are batched together
	 * and sent in a single request.
	 * 
	 * This is useful when displaying a list of Proxies.  Eg - consider a list of 50 proxies
	 * Rather than making  50 requests to the server, each for a single proxy, the 
	 * call is batched, making a single request, for 50 proxies */ 
	public class LoadProxyRequestBuffer implements IOperationBuffer
	{
		private var log:ILogger = LogUtil.getLogger(this);
		
		private var remoteObject:RemoteObject;
		private var incrementalDelayThreshold:int;
		private var maxDelayThreshold:int;

		private var incrementalTimer:Timer;
		private var maximumDelayTimer:Timer;
		
		// Indicates if we should wait until the incrementalTimer fires once more
		private var waitAnotherIncrement:Boolean;
		private var pendingRequests:ArrayCollection = new ArrayCollection();
		private var requestHash:Object = new Object(); // Hash of Key: Generated proxy key, value: ProxyLoadRequest
		private var _acceptingNewRequests:Boolean = true;
		public function get acceptingNewRequests():Boolean
		{
			return _acceptingNewRequests;
		}
		public function LoadProxyRequestBuffer(remoteObject:RemoteObject,incrementalDelayThreshold:int,maxDelayThreshold:int)
		{
			this.remoteObject = remoteObject;
			this.incrementalDelayThreshold = incrementalDelayThreshold;
			this.maxDelayThreshold = maxDelayThreshold;
			initalizeTimers();
		}
		
		public function bufferedSend(...args):AsyncToken
		{
			if (isWaiting)
			{
				extendDelayTimer()
			} else {
				startTimers()
			}
			var proxyLoadRequest:ProxyLoadRequest = generateProxyLoadRequest(args);
			addCallToPendingBatch(proxyLoadRequest);
			return proxyLoadRequest.internalAsyncToken;
		}
		
		internal function addCallToPendingBatch(loadRequest:ProxyLoadRequest):void
		{
			if (!pendingRequests) pendingRequests = new ArrayCollection();
			requestHash[loadRequest.requestKey] = loadRequest;
			pendingRequests.addItem(loadRequest);
		}
		private function generateProxyLoadRequest(args:Array):ProxyLoadRequest
		{
			var methodArgs:Array = args[0];
			var proxyKey:Object = methodArgs[0];
			var remoteClassName:String = methodArgs[1];
			var token:AsyncToken = new AsyncToken(null);
			var loadRequest:ProxyLoadRequest = new ProxyLoadRequest(proxyKey,remoteClassName,token);
			return loadRequest;
		}
		private function startTimers():void
		{
			incrementalTimer.start();
			maximumDelayTimer.start();
		}
		private function stopTimers():void
		{
			incrementalTimer.stop();
			maximumDelayTimer.stop();
		}
		private function extendDelayTimer():void
		{
			waitAnotherIncrement = true;
		}
		private function send():void
		{
			_acceptingNewRequests = false;
			var serverToken:AsyncToken = remoteObject.loadProxyBatch(pendingRequests.source);
			log.info("Sending batch of {0} requests" , pendingRequests.length);
//			var serverToken:AsyncToken = remoteObject.loadProxyBatch(pendingRequests);
			serverToken.addResponder(new Responder(batchLoadCompleted,batchLoadFailed));
		}
		private function initalizeTimers():void
		{
			incrementalTimer = new Timer(incrementalDelayThreshold);
			maximumDelayTimer = new Timer(maxDelayThreshold);
			incrementalTimer.addEventListener(TimerEvent.TIMER,onIncrementalTimerFired);
			maximumDelayTimer.addEventListener(TimerEvent.TIMER,onMaxDelayTimerFired);
		}
			

		private function onMaxDelayTimerFired(event:TimerEvent):void
		{
			log.info("Max delay to send batch reached - dispatching");
			stopTimers()
			send();
		}

		private function onIncrementalTimerFired(event:TimerEvent):void
		{
			if (waitAnotherIncrement)
			{
				log.info("Extending delay before sending batched request");
				waitAnotherIncrement = false;
			} else {
				stopTimers()
				log.info("Batch timed out without additional activity - sending batch");
				send();
			}
		}
		private function get isWaiting():Boolean
		{
			return incrementalTimer.running;
		}
		private function batchLoadCompleted(event:ResultEvent):void
		{
			var loadResults:ArrayCollection = event.result as ArrayCollection;
			trace("Processing result of " + loadResults.length + " tokens");
			var stopwatch:StopWatch = new StopWatch();
			stopwatch.start("All tokens");
			for each (var proxyLoadResult:ProxyLoadResult in loadResults)
			{
				var request:ProxyLoadRequest = requestHash[proxyLoadResult.requestKey];
				var requestResultEvent:ResultEvent = ResultEvent.createEvent(proxyLoadResult.result,request.internalAsyncToken);
				var tokenStopwatch:StopWatch = StopWatch.startNew("Inner token");
				request.internalAsyncToken.applyResult(requestResultEvent);
				tokenStopwatch.stopAndTrace();
			}
			stopwatch.stopAndTrace();
		}
		private function batchLoadFailed(event:FaultEvent):void
		{
			for each (var loadRequest:ProxyLoadRequest in pendingRequests)
			{
				var requestFaultEvent:FaultEvent = FaultEvent.createEvent(event.fault,loadRequest.internalAsyncToken);
				loadRequest.internalAsyncToken.applyFault(requestFaultEvent);
			}
		}
	}
}