package org.dphibernate.persistence.state;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


import org.dphibernate.core.IHibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionChangeUpdater extends PropertyChangeUpdater {

	private final ObjectChangeUpdater objectChangeUpdater;
	private final Logger log = LoggerFactory.getLogger(CollectionChangeUpdater.class);
	private ArrayList members;
	public CollectionChangeUpdater(CollectionChangeMessage propertyChangeMessage,
			IHibernateProxy entity, IProxyResolver proxyResolver, ObjectChangeUpdater objectChangeUpdater) {
		super(propertyChangeMessage,entity,proxyResolver);
		this.objectChangeUpdater = objectChangeUpdater;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectChangeResult> update() {
		ArrayList<ObjectChangeResult> result = new ArrayList<ObjectChangeResult>();
		List<ObjectChangeMessage> collectionMembers = getCollectionChangeMessage().getCollectionMembers();
		Collection entityCollection = getEntityCollection();
		if (entityCollection == null)
		{
			try {
				Method setter = getSetter();
				/* TODO : Would be nice to be clever about how we instantiate these...
				Class<? extends Collection> collectionClass = (Class<? extends Collection>) getGetter().getReturnType();
				entityCollection  = collectionClass.newInstance(); */
				entityCollection = new ArrayList();
				setter.invoke(entity, entityCollection);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for(ObjectChangeMessage member : collectionMembers)
		{
			
			Set<ObjectChangeResult> memberUpdateResult = objectChangeUpdater.update(member);
			result.addAll(memberUpdateResult);
			IHibernateProxyDescriptor targetProxy = member.getOwner();
			if (member.getIsNew())
			{
				// New object - see if the proxy resolver will find it as-is
				Object resolvedTarget = proxyResolver.resolve(targetProxy);
				if (resolvedTarget == null)
				{
					// We didn't find it - therefore, we should have a new updated proxyID to use when looking it up.
					ObjectChangeResult createNewResult = member.getResult();
					if (createNewResult == null)
					{
						// We couldn't resolve it.  Bitch.
						throw new RuntimeException("Message generated new entity, but result was not stored.");
					}
					targetProxy.setProxyId(createNewResult.getNewId());
					
				}
			}
			Object newMember = proxyResolver.resolve(targetProxy);
			if (!entityCollection.contains(newMember))
			{
				entityCollection.add(newMember);
			}
		}
		removeDeletedMembers();
		return result;
	}
	
	private void removeDeletedMembers() {
		ArrayList<Object> membersToRemove = new ArrayList<Object>();
		Collection sourceCollection = getEntityCollection();
		if (sourceCollection==null) return;
		for (Object sourceCollectionMember : sourceCollection)
		{
			if (sourceCollectionMember == null)
				continue;
			
			if (!getCollectionChangeMessage().containsProxyForEntity((IHibernateProxy) sourceCollectionMember))
			{
				membersToRemove.add(sourceCollectionMember);
			}
		}
		for (Object memberToRemove : membersToRemove)
		{
			sourceCollection.remove(memberToRemove);
		}
		
	}

	private Collection getEntityCollection() {
		return (Collection) getCurrentValue();
	}
	private CollectionChangeMessage getCollectionChangeMessage()
	{
		return (CollectionChangeMessage) propertyChangeMessage;
	}
}
