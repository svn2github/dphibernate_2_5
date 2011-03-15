package org.dphibernate.core;

public interface IEntity
{
	Object getEntityKey();
	void setEntityKey(Object obj);
	
	Boolean getEntityInitialized();
	void setEntityInitialized(Boolean b);	
}
