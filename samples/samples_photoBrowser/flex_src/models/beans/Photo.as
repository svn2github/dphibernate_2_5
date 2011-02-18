package models.beans
{
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="photoBrowser.beans.Photo")]
	[Managed]
	public class Photo extends HibernateBean
	{
		public var id:String;
		public var parent:MediaSet = null;
		public var name:String;
		public var url:String;
		public var width:int;
		public var height:int;
		public var photoDetails:PhotoDetails;
		public var ignore:Boolean = false;
	}
}
