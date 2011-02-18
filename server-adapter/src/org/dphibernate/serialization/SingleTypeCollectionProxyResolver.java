package org.dphibernate.serialization;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.dphibernate.persistence.state.HibernateProxyDescriptor;
import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.dialect.Dialect;
import org.hibernate.event.EventSource;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.sql.SimpleSelect;

public class SingleTypeCollectionProxyResolver extends AbstractCollectionProxyResolver implements CollectionProxyResolver
{

	private final Dialect dialect;
	private final AbstractCollectionPersister collectionPersister;
	public SingleTypeCollectionProxyResolver(Dialect dialect,AbstractCollectionPersister collectionPersister)
	{
		this.dialect = dialect;
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
			String className = collectionPersister.getElementType().getName();
			return new HibernateProxyDescriptor(className, (Serializable) key);
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
		pkSelect.addCondition(collectionPersister.getKeyColumnNames(), "=?");

		String sql = pkSelect.toStatementString();
		return sql;
	}
}
