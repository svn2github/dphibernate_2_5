package org.dphibernate.persistence.state;

import java.io.Serializable;

import org.dphibernate.core.IEntity;


public class HibernateProxyDescriptor implements IHibernateProxyDescriptor {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((proxyId == null) ? 0 : proxyId.hashCode());
		result = prime * result
				+ ((remoteClassName == null) ? 0 : remoteClassName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HibernateProxyDescriptor other = (HibernateProxyDescriptor) obj;
		if (proxyId == null) {
			if (other.proxyId != null)
				return false;
		} else if (!proxyId.equals(other.proxyId))
			return false;
		if (remoteClassName == null) {
			if (other.remoteClassName != null)
				return false;
		} else if (!remoteClassName.equals(other.remoteClassName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "HibernateProxyDescriptor [proxyId=" + proxyId
				+ ", remoteClassName=" + remoteClassName + "]";
	}
	private String remoteClassName;
	private Object proxyId;
	
	public HibernateProxyDescriptor(){}
	public HibernateProxyDescriptor(String remoteClassName, Serializable id)
	{
		this.remoteClassName = remoteClassName;
		this.proxyId = id;
	}
	public HibernateProxyDescriptor(Class<?> remoteClass, Serializable id)
	{
		this.remoteClassName = remoteClass.getName();
		this.proxyId = id;
	}
	public void setRemoteClassName(String remoteClassName) {
		this.remoteClassName = remoteClassName;
	}
	public String getRemoteClassName() {
		return remoteClassName;
	}
	public void setProxyId(Object proxyId) {
		this.proxyId = proxyId;
	}
	public Object getProxyId() {
		return proxyId;
	}
	public String getKey()
	{
		return remoteClassName + "::" + proxyId.toString();
	}
	public boolean matches(IEntity entity)
	{
		String objectClassName = entity.getClass().getName();
		if (!getRemoteClassName().equals(objectClassName))
			return false;
		if (proxyId.equals(entity.getEntityKey()))
			return true;
		if (proxyId instanceof String && entity.getEntityKey() instanceof Integer)
		{
			try
			{
				Integer parsedKey = Integer.parseInt((String)proxyId);
				return parsedKey.equals(entity.getEntityKey());
			} catch (NumberFormatException e)
			{
				return false;
			}
		}
		return false;
	}
	@Override
	public boolean isForClass(Class<? extends IEntity> proxyClass)
	{
		Class<?> remoteClass;
		try
		{
			remoteClass = Class.forName(remoteClassName);
		} catch (ClassNotFoundException e)
		{
			return false;
		}
		return proxyClass.isAssignableFrom(remoteClass);
	}
}
