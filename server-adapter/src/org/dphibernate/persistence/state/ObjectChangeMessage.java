package org.dphibernate.persistence.state;

import java.util.ArrayList;
import java.util.List;

import org.dphibernate.core.IEntity;


public class ObjectChangeMessage {

	private IHibernateProxyDescriptor owner;
	private boolean isNew;
	private boolean isDeleted;
	private List<PropertyChangeMessage> changedProperties;
	private ObjectChangeResult result;
	private Object createdEntity;

	public ObjectChangeMessage() {}
	public ObjectChangeMessage(IHibernateProxyDescriptor owner, boolean isNew)
	{
		this.owner = owner;
		this.isNew = isNew;
	}
	public static ObjectChangeMessage createChanged(IHibernateProxyDescriptor owner)
	{
		ObjectChangeMessage result = new ObjectChangeMessage();
		result.owner = owner;
		return result;
	}
	public static ObjectChangeMessage createNew(IHibernateProxyDescriptor owner)
	{
		ObjectChangeMessage result = new ObjectChangeMessage();
		result.owner = owner;
		result.isNew = true;
		return result;
	}
	public static ObjectChangeMessage createDeleted(IHibernateProxyDescriptor owner)
	{
		ObjectChangeMessage result = new ObjectChangeMessage();
		result.owner = owner;
		result.isDeleted = true;
		return result;
	}
	
	public void setChangedProperties(List<PropertyChangeMessage> changedProperties) {
		this.changedProperties = changedProperties;
	}
	public List<PropertyChangeMessage> getChangedProperties() {
		return changedProperties;
	}

	public void setOwner(IHibernateProxyDescriptor owner) {
		this.owner = owner;
	}

	public IHibernateProxyDescriptor getOwner() {
		return owner;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean getIsNew() {
		return isNew;
	}
	public void addChange(PropertyChangeMessage changeMessage) {
		if (changedProperties == null) changedProperties = new ArrayList<PropertyChangeMessage>();
		changedProperties.add(changeMessage);
	}
	public void addChanges(
			List<PropertyChangeMessage> messages) {
		if (changedProperties == null) changedProperties = new ArrayList<PropertyChangeMessage>();
		changedProperties.addAll(messages);
	}

	public boolean hasChanges()
	{
		if (changedProperties == null) return false;
		if (changedProperties.size() == 0) return false;
		return true;
	}
	
	public void setResult(ObjectChangeResult result)
	{
		this.result = result;
	}
	public ObjectChangeResult getResult()
	{
		return result;
	}
	public Object getCreatedEntity()
	{
		return this.createdEntity;
	}
	public void setCreatedEntity(Object value)
	{
		this.createdEntity = value;
	}
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public boolean getIsDeleted() {
		return isDeleted;
	}
	public boolean containsChangeToProperty(String propertyName)
	{
		for (PropertyChangeMessage pcm : changedProperties)
		{
			if (pcm.getPropertyName().equals(propertyName))
			{
				return true;
			}
		}
		return false;
	}
	public PropertyChangeMessage  getPropertyChange(String propertyName)
	{
		for (PropertyChangeMessage pcm : changedProperties)
		{
			if (pcm.getPropertyName().equals(propertyName))
			{
				return pcm;
			}
		}
		throw new RuntimeException("No change message exists for property " + propertyName);
	}
	public static ObjectChangeMessageBuilder buildFor(Class<? extends IEntity> proxyClass)
	{
		return new ObjectChangeMessageBuilder(proxyClass);
	}
}
