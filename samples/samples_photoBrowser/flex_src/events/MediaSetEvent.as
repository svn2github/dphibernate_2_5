package events
{
	import flash.events.Event;
	
	import models.beans.MediaSet;

	public class MediaSetEvent extends Event
	{
		public static const MEDIASET_SELECT:String = "mediaSetSelect";
		
		public var mediaSet:MediaSet;
		
		public function MediaSetEvent(type:String, mediaSet:MediaSet):void
		{
			super(type, true, true);
			this.mediaSet = mediaSet;
		}
		
		override public function clone():Event
		{
			return new MediaSetEvent(this.type, this.mediaSet);
		}
		
	}
}