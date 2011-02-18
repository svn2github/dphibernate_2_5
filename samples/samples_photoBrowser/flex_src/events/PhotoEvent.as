package events
{
	import flash.events.Event;
	
	import models.beans.Photo;

	public class PhotoEvent extends Event
	{
		public static const PHOTO_SELECT:String = "photoSelect";
		
		public var photo:Photo;
		
		public function PhotoEvent(type:String, photo:Photo)
		{
			super(type, true, true);
			this.photo = photo;
		}
		
		override public function clone():Event
		{
			return new PhotoEvent(this.type, this.photo);
		}
		
	}
}