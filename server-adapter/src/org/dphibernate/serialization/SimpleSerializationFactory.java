package org.dphibernate.serialization;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SimpleSerializationFactory implements ISerializerFactory
{
	protected SessionFactory sessionFactory;
	private SerializerConfiguration defaultConfiguration;

	public SimpleSerializationFactory()
	{
		initalizeSessionFactory();
	}
	@Override
	public IDeserializer getDeserializer()
	{
		return new HibernateDeserializer();
	}


	@Override
	public ISerializer getSerializer(Object source)
	{
		return getSerializer(source, false);
	}


	@Override
	public ISerializer getSerializer(Object source, boolean useAggressiveSerialization)
	{
		DPHibernateCache cache = new DPHibernateCache();
		SessionFactory sessionFactory = getSessionFactory();
		HibernateSerializer hibernateSerializer = new HibernateSerializer(source, useAggressiveSerialization, cache, sessionFactory);
		hibernateSerializer.configure(defaultConfiguration);
		return hibernateSerializer;
	}


	@Override
	public SessionFactory getSessionFactory()
	{
		if (sessionFactory == null)
		{
			initalizeSessionFactory();
		}
		return sessionFactory;
	}


	protected void initalizeSessionFactory()
	{
		try
		{
			// Create the SessionFactory
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex)
		{
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	@Override
	public void setDefaultConfiguration(SerializerConfiguration configuration)
	{
		this.defaultConfiguration = configuration;
	}

}
