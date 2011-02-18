package org.dphibernate.persistence.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dphibernate.core.IHibernateProxy;


public class ObjectChangeMessageBuilder
{

	private final Class<? extends IHibernateProxy> proxyClass;
	private Serializable id;
	private boolean isNew = false;
	private List<PropertyChangeMessage> propertyChangeMessages = new ArrayList<PropertyChangeMessage>();
	
	public ObjectChangeMessageBuilder(Class<? extends IHibernateProxy> proxyClass)
	{
		this.proxyClass = proxyClass;
	}

	public ObjectChangeMessage build()
	{
		assert(id != null);
		HibernateProxyDescriptor descriptor = new HibernateProxyDescriptor(proxyClass, id);
		ObjectChangeMessage changeMessage = new ObjectChangeMessage(descriptor,isNew);
		changeMessage.addChanges(propertyChangeMessages);
		return changeMessage;
	}
	public ObjectChangeMessageBuilder withId(Serializable id)
	{
		this.id = id;
		return this;
	}
	public ObjectChangeMessageBuilder asNew()
	{
		this.isNew = true;
		return this;
	}

	public PropertyChangeBuilder sets(String propertyName)
	{
		return new PropertyChangeBuilder(propertyName,this);
	}

	public void addPropertyChange(PropertyChangeMessage propertyChangeMessage)
	{
		propertyChangeMessages.add(propertyChangeMessage);
	}

}
