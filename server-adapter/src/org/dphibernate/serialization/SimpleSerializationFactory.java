package org.dphibernate.serialization;

import org.dphibernate.context.Context;

public class SimpleSerializationFactory implements ISerializerFactory
{
	private final Context context;
	private final SerializerConfiguration defaultConfiguration;

	public SimpleSerializationFactory(Context context,SerializerConfiguration defaultConfiguration)
	{
		this.context = context;
		this.defaultConfiguration = defaultConfiguration;
	}


	@Override
	public IDeserializer getDeserializer()
	{
		return new HibernateDeserializer();
	}


	@Override
	public SerializerBuilder createSerializerFor(Object source)
	{
		return new SerializerBuilder(source,context,defaultConfiguration);
	}

}
