package services
{
	import mx.messaging.*;
	import mx.messaging.channels.AMFChannel;
	import mx.rpc.IResponder;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import net.digitalprimates.persistence.hibernate.rpc.HibernateRemoteObject;
	
	import services.hibernate.MediaServiceHib;

	
	public class ServiceFactory
	{
		public static const USE_STUB_DATA:Boolean = false;

		public var _flexChannelSet:ChannelSet;
		// cached services
		private static var _serviceFactory:ServiceFactory;
		private static var _mediaService:HibernateRemoteObject;
		
						
		public static function getInstance():ServiceFactory 
		{
			if (_serviceFactory == null) {
				_serviceFactory = new ServiceFactory();	
			}			
			return _serviceFactory;
		}
		
		public function ServiceFactory() 
		{
 			var _flexChannel:Channel = new AMFChannel("my-amf", "http://mnimer-laptop:8080/samples_photoBrowser/messagebroker/amf");			
				_flexChannelSet = new ChannelSet();
				_flexChannelSet.addChannel(_flexChannel);
		}
		
		
		
		/**
		 * 
		 */
		public static function getMediaService(resp_:IResponder):IMediaService
		{
			
			if (!USE_STUB_DATA) 
			{
				if (_mediaService == null) 
				{
					_mediaService = new HibernateRemoteObject('mediaService');				
					_mediaService.channelSet = ServiceFactory.getInstance()._flexChannelSet;
					RemoteObject(_mediaService).showBusyCursor = true;				
				}
				return new MediaServiceHib(resp_, _mediaService);
			} else {
				return null;
				//var _stubService:StubDataService = new StubDataService();
				//return new MediaServiceStub(resp_, _stubService);
			}
		}
				
	}
}