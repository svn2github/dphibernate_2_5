package org.dphibernate
{
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flexunit.framework.Assert; /** Helper class for asserting that certain events occur and others don't */
	
	public class EventChecker
	{
		private var _source:IEventDispatcher;
		private var _expected:Array;
		private var _failEvents:Array;
		private var _received:Object;
		private var _errors:Array; /**     * Array containing any errors thrown by the event handlers that were called     * for expected events.     */
		
		public function get errors():Array
		{
			return _errors.slice();
		} /** True if any handlers for expected events threw an error. */
		
		public function get errorsOccurred():Boolean
		{
			return _errors.length > 0;
		} /**     * Constructs the EventChecker     * @param source The dispatcher that is the source of the events.     */
		
		public function EventChecker(source:IEventDispatcher)
		{
			_source=source;
			_expected=[];
			_failEvents=[];
			_received={};
			_errors=[];
		}
		
		/**     * Tells the checker that a particular event is expected.     *      * Any errors thrown by the handler (including assertion failures) will     * be captures and delayed until the EventChecker's assert() method is     * called.  This is due to the fact that FlexUnit doesn't seem to handle     * assertions made in event handlers (the built-in event handling     * mechanism sees the exception and treats it as unhandled).     *      * @param eventName The name of the event to expect     * @param handler An event hanlder to call when the event occurrs, or null     *    if none is needed.     * @param handlerScope The object to act as the handler's scope (if needed).     */
		
		public function expect(eventName:String, handler:Function=null, handlerScope:Object=null):void
		{
			_received[eventName]=false;
			_expected.push(eventName);
			_source.addEventListener(eventName, function(e:Event):void
			{
				_received[eventName]=true;
				if (handler != null)
				{
					handlerWrapper(handler, e, handlerScope);
				}
			});
		} /**     * Tells the checker that a particular event is not expected and should     * cause the test to fail.     * @param eventName The name of the event to fail on     */
		
		public function fail(eventName:String):void
		{
			_received[eventName]=false;
			_failEvents.push(eventName);
			_source.addEventListener(eventName, function(e:Event):void
			{
				_received[eventName]=true;
			});
		} /**     * Asserts that all expected events occurred and that none of the fail on     * events occurred.     */
		
		public function assert():void
		{
			var eventName:String;
			for each (eventName in _expected)
			{
				if (!_received[eventName])
				{
					Assert.fail("Expected event was not received: " + eventName);
				}
			}
			for each (eventName in _failEvents)
			{
				if (_received[eventName])
				{
					Assert.fail("Unexpected event was received: " + eventName);
				}
			}
			if (_errors.length > 0)
			{
				throw _errors[0];
			}
		}
		
		private function handlerWrapper(handler:Function, e:Event, scope:Object):void
		{
			try
			{
				handler.call(scope, e);
			}
			catch (e:Error)
			{
				_errors.push(e);
			}
		}
	}
}