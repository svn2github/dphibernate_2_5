package org.dphibernate.persistence.state;

import java.util.ArrayList;
import java.util.List;


import org.dphibernate.core.IHibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionChangeMessage extends PropertyChangeMessage {

	private final Logger log = LoggerFactory.getLogger(CollectionChangeMessage.class);
	
	public CollectionChangeMessage(){}
	public CollectionChangeMessage(String propertyName, Object oldValue, Object newValue)
	{
		super(propertyName,oldValue,newValue);
	}
	public CollectionChangeMessage(String propertyName, Object newValue)
	{
		// Note that at present we ignore the old value - concurrency checking is not
		// performed on collections
		super(propertyName,null,newValue);
	}
	public boolean containsProxyForEntity(IHibernateProxy entity)
	{
		String objectClassName = entity.getClass().getName();
		for (ObjectChangeMessage member : getCollectionMembers())
		{
			IHibernateProxyDescriptor owner = member.getOwner();
			if (owner.matches(entity))
			{
				return true;
			}
			if (member.getCreatedEntity() != null && member.getCreatedEntity().equals(entity))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<ObjectChangeMessage> getCollectionMembers()
	{
		ArrayList<ObjectChangeMessage> result = new ArrayList<ObjectChangeMessage>();
		for (Object object : (Object[]) this.getNewValue())
		{
			if (object instanceof ObjectChangeMessage)
			{
				result.add((ObjectChangeMessage) object);
			} else {
				log.warn("CollectionChangeMessage.newValue() contains an object which is not of type ObjectChangeMessage.  (" + object.getClass().getName() + ") Ignored");
			}
		}
		return result;
	}
}
