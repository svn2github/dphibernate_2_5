package modules.treeBrowser.commands
{
	import net.digitalprimates.framework.command.CommandAdapter;
	
	import services.ServiceFactory;

	public class GetAllMediaSetsCommand extends CommandAdapter
	{
		public function GetAllMediaSetsCommand()
		{
			super();
		}
		
		override public function execute():void
		{
			ServiceFactory.getMediaService(this).getAllMediaSets();
		}
		
		
	}
}