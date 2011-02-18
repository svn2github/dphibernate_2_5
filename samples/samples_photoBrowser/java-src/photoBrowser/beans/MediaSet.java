package photoBrowser.beans;

import java.util.Collection;
import java.util.HashSet;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;

public class MediaSet extends HibernateProxy implements IMediaChild
{
	public String id;
	public String name;
	public MediaSet parent = null;
	public Collection children = new HashSet();


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public MediaSet getParent()
	{
		return parent;
	}


	public void setParent(MediaSet parent)
	{
		this.parent = parent;
	}


	public Collection getChildren()
	{
		return children;
	}


	public void setChildren(Collection children)
	{
		this.children = children;
	}

}
