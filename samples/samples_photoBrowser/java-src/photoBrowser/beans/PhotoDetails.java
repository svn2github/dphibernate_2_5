package photoBrowser.beans;

import java.util.Date;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

public class PhotoDetails extends HibernateProxy
{
	public String id;
	public Photo photo;
	public Date dateTaken;
	public String copyright;
	public String exifModel;
	public String exifFNumber;
	public String exifIso;
	public String exifFocalLength;
	public String exifImageWidth;
	public String exifImageHeight;
	public String exifGpsInfo;
	public byte[] exifThumbnailData;
	public String exifThumbnailWidth;
	public String exifThumbnailHeight;


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public Photo getPhoto()
	{
		return photo;
	}


	public void setPhoto(Photo photo)
	{
		this.photo = photo;
	}


	public Date getDateTaken()
	{
		return dateTaken;
	}


	public void setDateTaken(Date dateTaken)
	{
		this.dateTaken = dateTaken;
	}


	public String getCopyright()
	{
		return copyright;
	}


	public void setCopyright(String copyright)
	{
		this.copyright = copyright;
	}


	public String getExifModel()
	{
		return exifModel;
	}


	public void setExifModel(String exifModel)
	{
		this.exifModel = exifModel;
	}


	public String getExifFNumber()
	{
		return exifFNumber;
	}


	public void setExifFNumber(String exifFNumber)
	{
		this.exifFNumber = exifFNumber;
	}


	public String getExifIso()
	{
		return exifIso;
	}


	public void setExifIso(String exifIso)
	{
		this.exifIso = exifIso;
	}


	public String getExifFocalLength()
	{
		return exifFocalLength;
	}


	public void setExifFocalLength(String exifFocalLength)
	{
		this.exifFocalLength = exifFocalLength;
	}


	public String getExifImageWidth()
	{
		return exifImageWidth;
	}


	public void setExifImageWidth(String exifImageWidth)
	{
		this.exifImageWidth = exifImageWidth;
	}


	public String getExifImageHeight()
	{
		return exifImageHeight;
	}


	public void setExifImageHeight(String exifImageHeight)
	{
		this.exifImageHeight = exifImageHeight;
	}


	public String getExifGpsInfo()
	{
		return exifGpsInfo;
	}


	public void setExifGpsInfo(String exifGpsInfo)
	{
		this.exifGpsInfo = exifGpsInfo;
	}


	public byte[] getExifThumbnailData()
	{
		return exifThumbnailData;
	}


	public void setExifThumbnailData(byte[] exifThumbnailData)
	{
		this.exifThumbnailData = exifThumbnailData;
	}


	public String getExifThumbnailWidth()
	{
		return exifThumbnailWidth;
	}


	public void setExifThumbnailWidth(String exifThumbnailWidth)
	{
		this.exifThumbnailWidth = exifThumbnailWidth;
	}


	public String getExifThumbnailHeight()
	{
		return exifThumbnailHeight;
	}


	public void setExifThumbnailHeight(String exifThumbnailHeight)
	{
		this.exifThumbnailHeight = exifThumbnailHeight;
	}

}
