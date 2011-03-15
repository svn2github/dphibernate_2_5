package org.dphibernate.serialization;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dphibernate.core.IEntity;
import org.dphibernate.persistence.state.HibernateProxyDescriptor;
import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.dialect.Dialect;
import org.hibernate.event.EventSource;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.sql.SimpleSelect;
import org.hibernate.transform.ResultTransformer;

/**
 * Handles resolving a PersistentCollection to the lazy proxies in a scenario
 * where there is a InheritenceStrategy present with a DiscriminatorValue.
 * 
 * Determines the correct class for each member, rather than relying on the role
 * declared by the collection
 * 
 * @author Marty Pitt
 * 
 */
public class DiscriminatedCollectionProxyResolver extends AbstractCollectionProxyResolver implements CollectionProxyResolver
{

	private final Dialect dialect;
	private final SingleTableEntityPersister entityPersister;
	private final AbstractCollectionPersister collectionPersister;


	public DiscriminatedCollectionProxyResolver(Dialect dialect, SingleTableEntityPersister entityPersister,AbstractCollectionPersister collectionPersister)
	{
		this.dialect = dialect;
		this.entityPersister = entityPersister;
		this.collectionPersister = collectionPersister;
	}


	@Override
	public List<IHibernateProxyDescriptor> getCollectionProxies(EventSource eventSource, PersistentCollection collection)
	{
		List<IHibernateProxyDescriptor> proxyDescriptors;
		try
		{
			proxyDescriptors = getProxyDescriptors(eventSource, collectionPersister, collection);
		} catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		return proxyDescriptors;
	}

	@Override
	protected IHibernateProxyDescriptor getProxyDescriptorFromResult(ResultSet keyResults)
	{
		try
		{
			Object key = keyResults.getObject(1);
			String subclassName = entityPersister.getSubclassForDiscriminatorValue(keyResults.getObject(2));
			return new HibernateProxyDescriptor(subclassName, (Serializable) key);
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	protected String getSQL(AbstractCollectionPersister collectionPersister)
	{
		String[] keyNames;
		if (collectionPersister.isOneToMany() || collectionPersister.isManyToMany())
		{
			keyNames = collectionPersister.getElementColumnNames();
		} else
		{
			keyNames = collectionPersister.getKeyColumnNames();
		}
		SimpleSelect pkSelect = new SimpleSelect(dialect);
		pkSelect.setTableName(collectionPersister.getTableName());
		pkSelect.addColumns(keyNames);
		pkSelect.addColumn(entityPersister.getDiscriminatorColumnName());
		pkSelect.addCondition(collectionPersister.getKeyColumnNames(), "=?");

		String sql = pkSelect.toStatementString();
		return sql;
	}
}
