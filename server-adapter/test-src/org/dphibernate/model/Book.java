package org.dphibernate.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Book extends BaseEntity
{
	@ManyToOne
	private Author author;
	private String title;
	public Book(){};
	public Book(Author author,String title, Object key)
	{
		super();
		this.author = author;
		this.title = title;
		setProxyKey(key);
	}
	public Book(Author author, String title)
	{
		this(author,title,null);
	}
	public Author getAuthor()
	{
		return author;
	}
	public void setAuthor(Author author)
	{
		this.author = author;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
}
