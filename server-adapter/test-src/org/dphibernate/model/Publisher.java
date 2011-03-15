package org.dphibernate.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.dphibernate.core.IEntity;

// This class explicity implements IEntity
@Entity
public class Publisher extends BaseEntity implements IEntity
{
	@Transient
	private boolean initialized;
	private String name;
	private String address;
	@OneToMany(mappedBy="publisher")
	private List<Author> authors;
	public Publisher(){};
	public static Publisher withNameAndId(String name,int id)
	{
		Publisher publisher = new Publisher();
		publisher.setId(id);
		publisher.name = name;
		return publisher;
	}
	public Publisher(String name, String address)
	{
		super();
		this.name = name;
		this.address = address;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAuthors(List<Author> authors)
	{
		this.authors = authors;
	}
	public List<Author> getAuthors()
	{
		return authors;
	}
	@Override
	public Boolean getEntityInitialized()
	{
		return initialized;
	}
	@Override
	public Object getEntityKey()
	{
		return getId();
	}
	@Override
	public void setEntityInitialized(Boolean b)
	{
			initialized = b;
	}
	@Override
	public void setEntityKey(Object obj)
	{
		setId((Integer) obj);
	}
}
