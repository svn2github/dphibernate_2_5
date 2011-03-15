package org.dphibernate.serialization;

public class SerializableProperty
{

	private final SerializationMode serializationMode;
	private final String name;
	private final Object value;

	public SerializableProperty(String name, Object value, SerializationMode serializationMode)
	{
		this.name = name;
		this.value = value;
		this.serializationMode = serializationMode;
	}

	public SerializationMode getSerializationMode()
	{
		return serializationMode;
	}
	
	public boolean shouldAggressivelyProxy()
	{
		return serializationMode == SerializationMode.AGGRESSIVELY_PROXY;
	}
	public boolean shouldEagerlySerialize()
	{
		return serializationMode == SerializationMode.EAGERLY_SERIALIZE;
	}

	public String getName()
	{
		return name;
	}

	public Object getValue()
	{
		return value;
	}
}
