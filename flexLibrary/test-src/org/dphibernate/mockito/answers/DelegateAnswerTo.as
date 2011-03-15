package org.dphibernate.mockito.answers
{
	import org.mockito.api.Answer;
	import org.mockito.api.StubbingContext;
	import org.mockito.api.StubbingContextAware;

	public class DelegateAnswerTo implements Answer, StubbingContextAware {
		
		private var context:StubbingContext;
		/**
		 * Delegates the answer to a given callback. Whenever the stubbed
		 * function is called, the call will be delegated to this callback, and the
		 * original arguments will be passed.
		 */
		private var delegateCallback:Function;
		
		public function DelegateAnswerTo(callback:Function)
		{
			delegateCallback = callback;
		}
		
		
		public function useContext(stubbingContext:StubbingContext):void
		{
			context = stubbingContext;
		}
		
		public function give():*
		{
			return delegateCallback.apply(null, context.args);
		}
	}
}