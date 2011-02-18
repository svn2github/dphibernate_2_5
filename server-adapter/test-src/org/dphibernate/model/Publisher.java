package org.dphibernate.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Publisher  extends BaseEntity
{
	private String name;
	private String address;
	@OneToMany(mappedBy="publisher")
	private List<Author> authors;
	public Publisher(){};
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
}
