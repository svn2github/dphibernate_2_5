package org.dphibernate.persistence.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
import org.dphibernate.persistence.state.ObjectChangeMessage;
import org.dphibernate.persistence.state.PropertyChangeMessage;


public class UpdateDependencyResolver {
	private Map<IHibernateProxyDescriptor,List<IHibernateProxyDescriptor>> messageProxyDependencies;
	Map<IHibernateProxyDescriptor,ObjectChangeMessage> messagesByOwner;
	public UpdateDependencyResolver()
	{
		setMessageProxyDependencies(new HashMap<IHibernateProxyDescriptor, List<IHibernateProxyDescriptor>>());
		messagesByOwner = new HashMap<IHibernateProxyDescriptor, ObjectChangeMessage>();
	}

	public void addMessages(List<ObjectChangeMessage> objectChangeMessages) {
		for (ObjectChangeMessage objectChangeMessage : objectChangeMessages)
		{
			messagesByOwner.put(objectChangeMessage.getOwner(), objectChangeMessage);
			parseMessage(objectChangeMessage);
		}
	}

	private void parseMessage(ObjectChangeMessage objectChangeMessage) {
		List<IHibernateProxyDescriptor> dependencies = new java.util.ArrayList<IHibernateProxyDescriptor>();
		for (PropertyChangeMessage propertyChangeMessage : objectChangeMessage.getChangedProperties())
		{
			if (propertyChangeMessage.getNewValue() instanceof IHibernateProxyDescriptor)
			{
				dependencies.add((IHibernateProxyDescriptor) propertyChangeMessage.getNewValue());
			}
			/*
			if (propertyChangeMessage instanceof CollectionChangeMessage)
			{
				dependencies.addAll(parseCollectionChangeMessage(((CollectionChangeMessage)propertyChangeMessage)));
			}
			*/
		}
		getMessageProxyDependencies().put(objectChangeMessage.getOwner(), dependencies);
	}

	private Collection<? extends IHibernateProxyDescriptor> parseCollectionChangeMessage(CollectionChangeMessage collectionChangeMessage)
	{
		Collection<IHibernateProxyDescriptor> dependencies = new ArrayList<IHibernateProxyDescriptor>();
		for (ObjectChangeMessage objectChangeMessage : collectionChangeMessage.getCollectionMembers())
		{
			dependencies.add(objectChangeMessage.getOwner());
		}
		return dependencies;
	}

	public List<ObjectChangeMessage> getOrderedList() {
		List<ObjectChangeMessage> messages = new ArrayList<ObjectChangeMessage>(messagesByOwner.values());
		
		orderList(messages);
		return messages;
	}

	private void orderList(List<ObjectChangeMessage> rawList) {
		for (int i=0;i<rawList.size();i++)
		{
			ObjectChangeMessage message = rawList.get(i);
			IHibernateProxyDescriptor owner = message.getOwner();
			List<IHibernateProxyDescriptor> dependencies = getMessageProxyDependencies().get(owner);
			if (dependencies.size()==0) continue;
			
			if (containsCircularDependency(owner,dependencies))
			{
				throw new RuntimeException("Circular dependency detected");
			}
			
			for(IHibernateProxyDescriptor dependency : dependencies){
				if (!messagesByOwner.containsKey(dependency)) continue;
				ObjectChangeMessage dependentMessage = messagesByOwner.get(dependency);
				if (!rawList.contains(dependentMessage))
				{
					throw new RuntimeException("Dependant message not found in messages collection");
				}
				int dependentMessageIndex = rawList.indexOf(dependentMessage);
				if (dependentMessageIndex > i)
				{
					Collections.swap(rawList, i, dependentMessageIndex);
					orderList(rawList);
					return;
				}
			}
		}
	}

	private boolean containsCircularDependency(IHibernateProxyDescriptor owner, List<IHibernateProxyDescriptor> dependencies) {
		// TODO
		return false;
	}

	public void setMessageProxyDependencies(Map<IHibernateProxyDescriptor,List<IHibernateProxyDescriptor>> messageProxyDependencies)
	{
		this.messageProxyDependencies = messageProxyDependencies;
	}

	public Map<IHibernateProxyDescriptor,List<IHibernateProxyDescriptor>> getMessageProxyDependencies()
	{
		return messageProxyDependencies;
	}
	
}
