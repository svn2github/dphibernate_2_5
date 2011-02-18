package photoBrowser.beans;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

public class Photo extends HibernateProxy implements IMediaChild
{
	public String id;
	public MediaSet parent;
	public String name;
	public String url;
	public Integer width;
	public Integer height;
	public PhotoDetails photoDetails;
	public Boolean ignore = false;


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public MediaSet getParent()
	{
		return parent;
	}


	public void setParent(MediaSet parent)
	{
		this.parent = parent;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getUrl()
	{
		return url;
	}


	public void setUrl(String url)
	{
		this.url = url;
	}


	public Integer getWidth()
	{
		return width;
	}


	public void setWidth(Integer width)
	{
		this.width = width;
	}


	public Integer getHeight()
	{
		return height;
	}


	public void setHeight(Integer height)
	{
		this.height = height;
	}


	public PhotoDetails getPhotoDetails()
	{
		return photoDetails;
	}


	public void setPhotoDetails(PhotoDetails photoDetails)
	{
		this.photoDetails = photoDetails;
	}


	public Boolean getIgnore()
	{
		return ignore;
	}


	public void setIgnore(Boolean ignore)
	{
		this.ignore = ignore;
	}

}
