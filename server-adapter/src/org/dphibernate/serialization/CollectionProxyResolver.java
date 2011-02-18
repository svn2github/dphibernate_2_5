package org.dphibernate.serialization;

import java.util.List;

import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.EventSource;
import org.hibernate.persister.collection.AbstractCollectionPersister;

public interface CollectionProxyResolver
{
	List<IHibernateProxyDescriptor> getCollectionProxies(EventSource eventSource, PersistentCollection collection);
}
