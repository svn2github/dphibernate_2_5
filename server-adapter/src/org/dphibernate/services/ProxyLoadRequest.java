package org.dphibernate.services;

import java.io.Serializable;
import java.rmi.server.UID;


import org.apache.commons.lang.builder.CompareToBuilder;
import org.dphibernate.core.IHibernateProxy;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

public class ProxyLoadRequest implements Comparable<ProxyLoadRequest>
{

	public ProxyLoadRequest()
	{}
	public ProxyLoadRequest(String className,Serializable proxyID,String requestKey)
	{
		this.className = className;
		this.proxyID = proxyID;
		this.requestKey = requestKey;
		setProxyClass();
	}
	private void setProxyClass()
	{
		try
		{
			this.proxyClass = Class.forName(className);
		} catch (ClassNotFoundException e)
		{
			throw new RuntimeException("Requested proxy class " + className + " does not exist");
		}

	}
	public ProxyLoadRequest(String className,Serializable proxyID)
	{
		this(className,proxyID,new UID().toString());
	}
	private String className;
	private Serializable proxyID;
	private String requestKey;
	private Class<?> proxyClass;
	public void setClassName(String className)
	{
		this.className = className;
		setProxyClass();
	}
	public String getClassName()
	{
		return className;
	}
	public void setProxyID(Serializable proxyID)
	{
		this.proxyID = proxyID;
	}
	public Serializable getProxyID()
	{
		return proxyID;
	}
	public void setRequestKey(String requestKey)
	{
		this.requestKey = requestKey;
	}
	public String getRequestKey()
	{
		return requestKey;
	}
	@Override
	public int compareTo(ProxyLoadRequest o)
	{
		return this.className.compareTo(o.className);
	}
	public boolean matchesEntity(IHibernateProxy entity)
	{
		boolean classesMatch = entity.getClass().isAssignableFrom(proxyClass); 
		return classesMatch
				&& entity.getProxyKey().equals(this.getProxyID());
	}
	@Override
	public String toString()
	{
		return "ProxyLoadRequest [className=" + className + ", proxyID=" + proxyID + "]";
	}
}
