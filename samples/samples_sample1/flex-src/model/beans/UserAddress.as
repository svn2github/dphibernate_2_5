package model.beans
{
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="net.digitalprimates.samples.sample1.beans.UserAddress")]
	[Managed]
	public dynamic class UserAddress extends HibernateBean
	{
		public var id:String;
		public var address1:String;
		public var address2:String;
		public var city:String;
		public var state:String;
		public var zip:String;
		public var user:User;
	    
	}
}