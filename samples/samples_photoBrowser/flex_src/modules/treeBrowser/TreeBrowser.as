package modules.treeBrowser
{
	import events.MediaSetEvent;
	import events.PhotoEvent;
	
	import models.beans.MediaSet;
	import models.beans.Photo;
	
	import modules.treeBrowser.commands.GetAllMediaSetsCommand;
	
	import mx.collections.ArrayCollection;
	import mx.containers.Canvas;
	import mx.events.FlexEvent;
	import mx.events.ListEvent;
	import mx.rpc.events.ResultEvent;

	public class TreeBrowser extends Canvas
	{
		public function TreeBrowser()
		{
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteEventHandler, false, 0, true);
		}
		
		
		private var _mediaCollection:ArrayCollection;
		[Bindable]
		public function get mediaCollection():ArrayCollection
		{
			return _mediaCollection;
		}

		public function set mediaCollection(ac:ArrayCollection):void
		{
			_mediaCollection = ac;
		}
		
		
		protected function nameLabel(item:Object):String
		{
			var label:String = item.name; 
			return label
		}
		
		
		protected function selectItem(event:ListEvent):void
		{
			if(event.currentTarget.selectedItem is MediaSet)
			{
				var mediaEvent:MediaSetEvent = new MediaSetEvent(MediaSetEvent.MEDIASET_SELECT, MediaSet(event.currentTarget.selectedItem));
				dispatchEvent(mediaEvent);
			}
			else if(event.currentTarget.selectedItem is Photo)
			{
				var photoEvent:PhotoEvent = new PhotoEvent(PhotoEvent.PHOTO_SELECT, Photo(event.currentTarget.selectedItem));
				dispatchEvent(photoEvent);
			}
		}
		
		
		public function creationCompleteEventHandler(event:FlexEvent):void
		{
			new GetAllMediaSetsCommand().setResult(this, "mediaCollection").execute();
		}
		
	}
}