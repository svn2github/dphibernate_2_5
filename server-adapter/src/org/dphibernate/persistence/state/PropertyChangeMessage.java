package org.dphibernate.persistence.state;

public class PropertyChangeMessage {
	private String propertyName;
	private Object oldValue;
	private Object newValue;
	
	public PropertyChangeMessage(){}
	public PropertyChangeMessage(String propertyName, Object oldValue, Object newValue)
	{
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	public Object getOldValue() {
		return oldValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	public Object getNewValue() {
		return newValue;
	}
	
}
