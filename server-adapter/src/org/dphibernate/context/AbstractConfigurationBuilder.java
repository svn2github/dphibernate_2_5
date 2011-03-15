package org.dphibernate.context;

import org.dphibernate.serialization.SerializationMode;
import org.dphibernate.serialization.SerializerConfiguration;

public abstract class AbstractConfigurationBuilder<T extends AbstractConfigurationBuilder>
{
	private final SerializerConfiguration configuration;
	public AbstractConfigurationBuilder()
	{
		this(new SerializerConfiguration());
	}
	public AbstractConfigurationBuilder(SerializerConfiguration configuration)
	{
		this.configuration = configuration;
	}

	public T usesAggressiveProxying()
	{
		configuration.setMode(SerializationMode.AGGRESSIVELY_PROXY);
		return (T) this;
	}
	public T uses(SerializationMode serializationMode)
	{
		configuration.setMode(serializationMode);
		return (T) this;
	}
	public T hasPageSize(int pageSize)
	{
		configuration.setPageSize(pageSize);
		return (T) this;
	}
	public SerializerConfiguration build()
	{
		return configuration;
	}
}
