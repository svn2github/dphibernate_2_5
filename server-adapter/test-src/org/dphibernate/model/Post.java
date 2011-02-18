package org.dphibernate.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Entity
@DiscriminatorColumn(name="postType", discriminatorType=DiscriminatorType.STRING)
public class Post extends BaseEntity {

	@ManyToOne
	private Author author;

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public Author getAuthor()
	{
		return author;
	}

}
