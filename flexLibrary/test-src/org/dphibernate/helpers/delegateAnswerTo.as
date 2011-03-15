package org.dphibernate.helpers
{
	import org.dphibernate.mockito.answers.DelegateAnswerTo;
	import org.mockito.api.Answer;

	public function delegateAnswerTo(delegateFunction:Function):Answer
	{
		return new DelegateAnswerTo(delegateFunction);
	}
}