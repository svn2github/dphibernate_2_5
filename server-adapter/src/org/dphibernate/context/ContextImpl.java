package org.dphibernate.context;

import org.dphibernate.proxy.EntityProxyFactory;
import org.dphibernate.serialization.IDeserializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerBuilder;
import org.dphibernate.serialization.SerializerConfiguration;
import org.dphibernate.serialization.WriterFactory;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;

public class ContextImpl implements Context
{

	ContextImpl(ContextBuilder builder)
	{
		this.dialect = builder.getDialect();
		this.sessionFactory = builder.getSessionFactory();
		this.proxyFactory = builder.getProxyFactory();
		this.serializerFactory = builder.buildSerializerFactory(this);
		this.writerFactory = builder.buildWriterFactory(this);
		this.defaultConfiguration = builder.getDefaultConfiguration();
	}
	private final Dialect dialect;
	private final WriterFactory writerFactory;
	private final SessionFactory sessionFactory;
	private final EntityProxyFactory proxyFactory;
	private final ISerializerFactory serializerFactory;
	private final SerializerConfiguration defaultConfiguration;

	@Override
	public Dialect getDialect()
	{
		return dialect;
	}


	public EntityProxyFactory getProxyFactory()
	{
		return proxyFactory;
	}


	public WriterFactory getWriterFactory()
	{
		return writerFactory;
	}


	@Override
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}


	public ISerializerFactory getSerializerFactory()
	{
		return serializerFactory;
	}


	@Override
	public SerializerBuilder createSerializerFor(Object source)
	{
		return serializerFactory.createSerializerFor(source);
	}


	@Override
	public IDeserializer getDeserializer()
	{
		return serializerFactory.getDeserializer();
	}


	@Override
	public SerializerConfiguration getDefaultConfiguration()
	{
		return defaultConfiguration.clone();
	}

}
