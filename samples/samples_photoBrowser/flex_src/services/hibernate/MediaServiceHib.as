package services.hibernate
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	import net.digitalprimates.framework.filters.CacheFilter;
	import net.digitalprimates.framework.filters.ErrorFilter;
	import net.digitalprimates.framework.filters.IAsyncFilter;
	import net.digitalprimates.framework.filters.ServiceInvokeFilter;
	import net.digitalprimates.framework.filters.dpHibernateFilter;
	import net.digitalprimates.framework.services.ServiceFilterAdapter;
	import net.digitalprimates.persistence.hibernate.rpc.HibernateRemoteObject;
	
	import services.IMediaService;
	
	public class MediaServiceHib extends ServiceFilterAdapter implements IMediaService
	{
		
		public function MediaServiceHib(resp_:IResponder, service_:HibernateRemoteObject)
		{
			super(resp_, service_);
		}

		
		override public function getFilterChain(method:String, args:Object):IAsyncFilter
		{
			var filter:IAsyncFilter = new ServiceInvokeFilter(this.service, method, args);
				filter = new dpHibernateFilter(filter, this.service);
			return filter;
		}


		public function getAllMediaSets():AsyncToken
		{
			return super.invoke("getAllMediaSets", null);
		}


		public function save(object:Object):AsyncToken
		{
			return super.invoke("save", [object]);
		}
		
		
		public function initializeDatabase(dir_:String):AsyncToken
		{
			return super.invoke("", [dir_]);
		}
	}
}