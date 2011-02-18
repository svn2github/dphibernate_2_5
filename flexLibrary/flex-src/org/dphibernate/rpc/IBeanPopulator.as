package org.dphibernate.rpc
{
	import flash.utils.Dictionary;
	

	public interface IBeanPopulator
	{
		function populateBean( genericObj:Object, 
												classDefinition:Class, 
												existingBean:Object=null, 
												dictionary:Dictionary=null,
												parent:Object=null,
												parentProperty:String=null, 
												ro:IHibernateRPC=null ):Object;	
	}
}