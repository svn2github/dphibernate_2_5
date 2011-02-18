package services
{
	import mx.rpc.AsyncToken;

	public interface IMediaService
	{
		function getAllMediaSets():AsyncToken;
		function save(object:Object):AsyncToken;
		function initializeDatabase(serverDir:String):AsyncToken;
	}
}