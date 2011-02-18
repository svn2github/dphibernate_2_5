package model.beans
{
	import mx.collections.ArrayCollection;
	
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="net.digitalprimates.samples.sample1.beans.User")]
	[Managed]
	public class User extends HibernateBean
	{
		public var id:String;
		public var firstName:String;
		public var lastName:String;
		public var addresses:ArrayCollection;
		public var addressList:ArrayCollection;
		//public var address2:ArrayCollection;
		public var connectInfo:UserConnectInfo;
	}
}