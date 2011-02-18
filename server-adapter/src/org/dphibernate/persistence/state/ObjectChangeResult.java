package org.dphibernate.persistence.state;

import java.io.Serializable;

public class ObjectChangeResult {

	private final Object oldId;
	private final Object newId;
	private final String remoteClassName;
	
	public ObjectChangeResult(String className,Object oldId,Object newId)
	{
		this.oldId = oldId;
		this.newId = newId;
		this.remoteClassName = className;
	}
	public ObjectChangeResult(Class<?> entityClass,Object oldId,Object newId)
	{
		this(entityClass.getName(),oldId,newId);
	}
	public ObjectChangeResult(ObjectChangeMessage changeMessage,Object newId)
	{
		this.remoteClassName = changeMessage.getOwner().getRemoteClassName();
		this.oldId = changeMessage.getOwner().getProxyId();
		this.newId = newId;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newId == null) ? 0 : newId.hashCode());
		result = prime * result + ((oldId == null) ? 0 : oldId.hashCode());
		result = prime * result + ((remoteClassName == null) ? 0 : remoteClassName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectChangeResult other = (ObjectChangeResult) obj;
		if (newId == null)
		{
			if (other.newId != null)
				return false;
		} else if (!newId.equals(other.newId))
			return false;
		if (oldId == null)
		{
			if (other.oldId != null)
				return false;
		} else if (!oldId.equals(other.oldId))
			return false;
		if (remoteClassName == null)
		{
			if (other.remoteClassName != null)
				return false;
		} else if (!remoteClassName.equals(other.remoteClassName))
			return false;
		return true;
	}
	public String getRemoteClassName() 
	{
		return remoteClassName;
	}
	
	
	public Object getNewId() {
		return newId;
	}
	public Object getOldId()
	{
		return oldId;
	}

}
