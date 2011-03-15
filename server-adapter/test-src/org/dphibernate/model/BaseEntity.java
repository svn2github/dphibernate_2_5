package org.dphibernate.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.dphibernate.core.IEntity;



@MappedSuperclass
public class BaseEntity
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	public void setId(Integer id)
	{
		this.id = id;
	}


	public Integer getId()
	{
		return id;
	}

}
