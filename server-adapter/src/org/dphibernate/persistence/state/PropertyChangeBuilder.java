package org.dphibernate.persistence.state;

public class PropertyChangeBuilder
{

	private final String propertyName;
	private Object newValue;
	private Object oldValue;
	private final ObjectChangeMessageBuilder objectChangeMessageBuilder;

	public PropertyChangeBuilder(String propertyName, ObjectChangeMessageBuilder objectChangeMessageBuilder)
	{
		this.propertyName = propertyName;
		// TODO Auto-generated constructor stub
		this.objectChangeMessageBuilder = objectChangeMessageBuilder;
	}

	public ObjectChangeMessageBuilder to(Object newValue)
	{
		this.newValue = newValue;
		PropertyChangeMessage propertyChangeMessage = new PropertyChangeMessage(propertyName, oldValue, newValue);
		objectChangeMessageBuilder.addPropertyChange(propertyChangeMessage);
		return objectChangeMessageBuilder;
	}
	public PropertyChangeBuilder from(Object oldValue)
	{
		this.oldValue = oldValue;
		return this;
	}
	
	

}
