package org.dphibernate.persistence.state
{
	public class ReadOnlyError extends Error
	{
		public function ReadOnlyError(message:String="This property is read-only", id:int=0)
		{
			super(message, id);
		}
		
	}
}