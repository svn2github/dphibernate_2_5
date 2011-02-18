package org.dphibernate.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.dphibernate.core.IHibernateProxy;



@MappedSuperclass
public class BaseEntity implements IHibernateProxy
{
	@Transient
	private Boolean proxyInitialized = true;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Override
	public Boolean getProxyInitialized()
	{
		return proxyInitialized;
	}


	@Override
	public Object getProxyKey()
	{
		return id;
	}


	@Override
	public void setProxyInitialized(Boolean b)
	{
		proxyInitialized = b;
	}


	@Override
	public void setProxyKey(Object obj)
	{
		id = (Integer)obj;
	}


	public void setId(Integer id)
	{
		this.id = id;
	}


	public Integer getId()
	{
		return id;
	}

}
