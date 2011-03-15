package org.dphibernate.serialization;

import org.dphibernate.context.Context;

public class SerializerBuilder
{
	private final Object source;
	private SerializerCache cache;
	private final Context context;
	private SerializerConfiguration configuration;
	public SerializerBuilder(Object source,Context context,SerializerConfiguration configuration)
	{
		this.configuration = configuration.clone();
		this.context = context;
		this.source = source;
		cache = new SerializerCache();
	}
	public SerializerBuilder usingCache(SerializerCache cache)
	{
		this.cache = cache;
		return this;
	}
	public SerializerBuilder thatAggressivelyProxies()
	{
		configuration.setMode(SerializationMode.AGGRESSIVELY_PROXY);
		return this;
	}
	
	public ISerializer build()
	{
		return new HibernateSerializer(this);
	}
	public SerializerBuilder and()
	{
		return this;
	}
	public SerializerBuilder permitsAggressiveProxyingOnRoot()
	{
		configuration.setPermitAgressiveProxyingOnRoot(true);
		return this;
	}
	Object getSource()
	{
		return source;
	}
	public Context getContext()
	{
		return context;
	}
	public SerializerConfiguration getConfiguration()
	{
		return configuration;
	}
	public SerializerCache getCache()
	{
		return cache;
	}
}
