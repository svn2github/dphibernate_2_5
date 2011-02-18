package org.dphibernate.serialization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
import org.hibernate.Session;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

public abstract class AbstractCollectionProxyResolver
{

	public AbstractCollectionProxyResolver()
	{
		super();
	}
	protected List<IHibernateProxyDescriptor> getProxyDescriptors(Session session, AbstractCollectionPersister persister, PersistentCollection collection) throws ClassNotFoundException
	{
		String sql = getSQL(persister);
		List<IHibernateProxyDescriptor> descriptors = getDescriptorsFromSQL(session, persister, collection, sql);
		return descriptors;
	}

	protected abstract String getSQL(AbstractCollectionPersister persister);
	protected abstract IHibernateProxyDescriptor getProxyDescriptorFromResult(ResultSet keyResults);
	
	protected List<IHibernateProxyDescriptor> getDescriptorsFromSQL(Session session, AbstractCollectionPersister persister, PersistentCollection collection,String sql)
	{
		List<IHibernateProxyDescriptor> results = new ArrayList<IHibernateProxyDescriptor>();
		try
		{
			PreparedStatement stmt = getPreparedStatement(session, persister, collection, sql);
			ResultSet keyResults = stmt.executeQuery();
	
			while (keyResults.next())
			{
				IHibernateProxyDescriptor proxyDescriptor = getProxyDescriptorFromResult(keyResults);
				results.add(proxyDescriptor);
			}
			stmt.close();
	
		} catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		return results;
	}


	protected PreparedStatement getPreparedStatement(Session session, AbstractCollectionPersister persister, PersistentCollection collection, String sql) throws SQLException
	{
		Type t = persister.getKeyType();
	
		PreparedStatement stmt = session.connection().prepareStatement(sql);
		if (t instanceof StringType)
		{
			stmt.setString(1, collection.getKey().toString());
		} else
		{
			stmt.setObject(1, new Integer(collection.getKey().toString()).intValue());
		}
		return stmt;
	}

}