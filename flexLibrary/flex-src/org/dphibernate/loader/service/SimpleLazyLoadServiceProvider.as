package org.dphibernate.loader.service
{
	import mx.messaging.ChannelSet;

	import org.dphibernate.core.IEntity;

	public class SimpleLazyLoadServiceProvider implements ILazyLoadServiceProvider
	{
		public var destination:String
		public var channelSet:ChannelSet;

		public function SimpleLazyLoadServiceProvider(destination:String, channelSet:ChannelSet)
		{
			super();
			this.destination=destination;
			this.channelSet=channelSet;
		}

		public function getLoader(entity:IEntity):ILazyLoadService
		{
			var service:RemotingLazyLoadService=new RemotingLazyLoadService(destination);
			service.channelSet = channelSet;
			return service;
		}
	}
}