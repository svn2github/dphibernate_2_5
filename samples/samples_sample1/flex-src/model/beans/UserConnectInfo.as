package model.beans
{
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="net.digitalprimates.samples.sample1.beans.UserConnectInfo")]
	[Managed]
	public dynamic class UserConnectInfo extends HibernateBean
	{
		public var id:String;
		public var email:String;
		public var yahooIM:String;
		public var aolIM:String;
		public var user:User;
		
	}
}