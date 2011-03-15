package org.dphibernate.context;

import org.apache.commons.lang.NotImplementedException;
import org.dphibernate.proxy.EntityIdentityProvider;
import org.dphibernate.proxy.EntityProxyFactory;
import org.dphibernate.proxy.HibernateIdentityProvider;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerConfiguration;
import org.dphibernate.serialization.SimpleSerializationFactory;
import org.dphibernate.serialization.WriterFactory;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.impl.SessionFactoryImpl;

/**
 * Builder for dpHibernate contexts
 * @author Marty Pitt
 *
 */
public class ContextBuilder
{
	private Dialect dialect;
	private SerializerConfiguration defaultConfiguration;
	private SessionFactory sessionFactory;
	private ISerializerFactory serializerFactory;
	private EntityIdentityProvider identityProvider;
	private ContextBuilder(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		defaultConfiguration = new SerializerConfiguration();
	}
	
	public static ContextBuilder with(SessionFactory sessionFactory)
	{
		ContextBuilder contextBuilder = new ContextBuilder(sessionFactory);
		return contextBuilder;
	}
	public ContextConfigBuilder withDefaultConfigurationThat()
	{
		return new ContextConfigBuilder(this, defaultConfiguration);
	}

	SerializerConfiguration getDefaultConfiguration()
	{
		return defaultConfiguration;
	}
	Dialect getDialect()
	{
		if (dialect == null)
		{
			initializeDialect();
		}
		return dialect;
	}
	private void initializeDialect()
	{
		try
		{
			dialect = Dialect.getDialect();
		} catch (Throwable t) {
			SessionFactoryImpl sfi = (SessionFactoryImpl) sessionFactory;
			dialect = sfi.getDialect();
		}
		if (dialect == null)
		{
			throw new RuntimeException("Could not determine dialect");
		}
	}

	EntityProxyFactory getProxyFactory()
	{
		return new EntityProxyFactory(getIdentityProvider());
	}

	private EntityIdentityProvider getIdentityProvider()
	{
		if (identityProvider == null)
		{
			identityProvider = new HibernateIdentityProvider(sessionFactory);
		}
		return identityProvider;
	}

	SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	ISerializerFactory buildSerializerFactory(Context context)
	{
		if (serializerFactory == null)
		{
			serializerFactory = new SimpleSerializationFactory(context,defaultConfiguration);
		}
		return serializerFactory;
	}
	
	public Context build()
	{
		/*
		 * Note, the relationship between this builder and the
		 * context is kinda messed up at the moment.
		 * 
		 * While the builder can build most things,
		 * the WriterFactory depends on a context.
		 * Therefore, the builder initializes the context
		 * with itself, and the context asks for everything.
		 * When asking for the writer factory, we need the
		 * context passed back to us.
		 * 
		 * Could probably come up with a cleaner impl., another time
		 */
		return new ContextImpl(this);
	}

	WriterFactory buildWriterFactory(ContextImpl contextImpl)
	{
		return new WriterFactory(contextImpl);
	}
}
class ContextConfigBuilder extends AbstractConfigurationBuilder<ContextConfigBuilder>
{
	private final ContextBuilder builder;
	public ContextConfigBuilder(ContextBuilder builder,SerializerConfiguration config)
	{
		super(config);
		this.builder = builder;
	}
	public ContextBuilder andContextThat()
	{
		return builder;
	}
}
