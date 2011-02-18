package models.beans
{
	import mx.collections.ArrayCollection;
	
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="photoBrowser.beans.MediaSet")]
	[Managed]
	public class MediaSet extends HibernateBean
	{
		public var id:String;
		public var name:String;
		public var parent:MediaSet = null;
		public var children:ArrayCollection = new ArrayCollection();
	
	}
}
