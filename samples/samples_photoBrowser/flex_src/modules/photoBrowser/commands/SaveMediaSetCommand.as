package modules.photoBrowser.commands
{
	import models.beans.MediaSet;
	
	import net.digitalprimates.framework.command.CommandAdapter;
	
	import services.ServiceFactory;

	public class SaveMediaSetCommand extends CommandAdapter
	{
		private var mediaSet:MediaSet;
		
		public function SaveMediaSetCommand(media_:MediaSet)
		{
			super();
			this.mediaSet = media_;
		}
		
		override public function execute():void
		{
			ServiceFactory.getMediaService(this).save(this.mediaSet);
		}
		
	}
}