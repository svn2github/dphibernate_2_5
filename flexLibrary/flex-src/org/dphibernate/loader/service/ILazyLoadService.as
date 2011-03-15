package org.dphibernate.loader.service
{
	import mx.rpc.AsyncToken;
	
	import org.dphibernate.core.IEntity;

	public interface ILazyLoadService
	{
		function loadProxy( entityKey:Object, remoteClassName:String ):AsyncToken;
	}
}