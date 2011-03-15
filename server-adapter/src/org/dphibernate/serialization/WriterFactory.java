package org.dphibernate.serialization;

import java.util.Collection;
import java.util.Map;

import org.dphibernate.context.Context;
import org.dphibernate.core.IEntity;
import org.dphibernate.serialization.writers.AbstractPersistentCollectionWriter;
import org.dphibernate.serialization.writers.ArrayWriter;
import org.dphibernate.serialization.writers.CollectionWriter;
import org.dphibernate.serialization.writers.DpHibernateProxyWriter;
import org.dphibernate.serialization.writers.EntityWriter;
import org.dphibernate.serialization.writers.MapWriter;
import org.dphibernate.serialization.writers.ObjectWriter;
import org.dphibernate.serialization.writers.PersistentMapWriter;
import org.dphibernate.serialization.writers.ProxiedEntityWriter;
import org.dphibernate.serialization.writers.SimpleWriter;
import org.dphibernate.utils.EntityUtil;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.collection.PersistentMap;
import org.hibernate.metadata.ClassMetadata;

import flex.messaging.io.amf.ASObject;

public class WriterFactory
{
	private final Context context;
	public WriterFactory(Context context)
	{
		this.context = context;
	}
	public ISerializationWriter<?,?> getWriter(Object objectToSerialize,SerializerCache cache)
	{
		return getWriter(objectToSerialize,cache,SerializationMode.NORMAL,context.getDefaultConfiguration());
	}
	public ISerializationWriter<?,?> getWriter(Object objectToSerialize,SerializerCache cache,SerializerConfiguration configuration)
	{
		return getWriter(objectToSerialize,cache,SerializationMode.NORMAL,configuration);
	}
	public ISerializationWriter<?,?> getWriter(Object objectToSerialize, SerializerCache cache,SerializationMode serializationMode,SerializerConfiguration configuration)
	{
		ISerializationWriter<?,?> writer;
		if (objectToSerialize == null)
		{
			writer = new SimpleWriter();
		} else if (EntityUtil.isLazyProxy(objectToSerialize) && !serializationMode.eagerlySerialize())
		{
			writer = new DpHibernateProxyWriter();
		} else if (configuration.shouldAggressivelyProxy(objectToSerialize,!serializationMode.eagerlySerialize()))
		{
			writer = new DpHibernateProxyWriter();
		} else if (objectToSerialize instanceof PersistentMap)
		{
			writer = new PersistentMapWriter(configuration.getPropertySerializer());
		} else if (objectToSerialize instanceof AbstractPersistentCollection)
		{
			writer = new AbstractPersistentCollectionWriter(configuration.getPropertySerializer(),serializationMode,context.getSessionFactory(),context.getDialect());
		} else if (objectToSerialize instanceof byte[]) 
		{
			writer = new SimpleWriter();
		} else if (objectToSerialize.getClass().isArray())
		{
			writer = new ArrayWriter(configuration.getPropertySerializer());
		} else if (objectToSerialize instanceof Collection)
		{
			writer = new CollectionWriter(configuration.getPropertySerializer());
		} else if (objectToSerialize instanceof Map)
		{
			writer = new MapWriter(configuration.getPropertySerializer());
		} else if (isEntity(objectToSerialize) && !(objectToSerialize instanceof IEntity))
		{
			writer = new ProxiedEntityWriter(context.getProxyFactory(),cache,context.getSerializerFactory(),configuration.getPropertySerializer());
		} else if (isEntity(objectToSerialize))
		{
			writer = new EntityWriter(cache,context.getSerializerFactory(),configuration.getPropertySerializer());
		} else if (objectToSerialize instanceof Object && (!TypeHelper.isSimple(objectToSerialize)) && !(objectToSerialize instanceof ASObject))
		{
			writer = new ObjectWriter(cache,context.getSerializerFactory(), configuration.getPropertySerializer());
		} else
		{
			writer = new SimpleWriter();
		}
		return writer;
	}
	boolean isEntity(Object objectToSerialize)
	{
		ClassMetadata metadata = context.getSessionFactory().getClassMetadata(objectToSerialize.getClass());
		return metadata != null;
	}
}
