package models.beans
{
	import flash.utils.ByteArray;
	
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="photoBrowser.beans.PhotoDetail")]
	[Managed]
	public class PhotoDetails extends HibernateBean
	{
		public var id:String;
		public var photo:Photo;
		public var dateTaken:Date;
	
		public var copyright:String;
		public var exifModel:String;
		public var exifFNumber:String;
		public var exifIso:String;
		public var exifFocalLength:String;
		public var exifImageWidth:String;
		public var exifImageHeight:String;
		public var exifGpsInfo:String;
		public var exifThumbnailData:ByteArray;
		public var exifThumbnailWidth:String;
		public var exifThumbnailHeight:String;

	}
}
