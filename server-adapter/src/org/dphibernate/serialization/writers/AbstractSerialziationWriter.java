package org.dphibernate.serialization.writers;

import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.SerializationMode;
import org.dphibernate.utils.ClassUtil;

public abstract class AbstractSerialziationWriter
{
	private final IPropertySerializer serializer;

	public AbstractSerialziationWriter(IPropertySerializer serializer)
	{
		this.serializer = serializer;
	}
	
	protected Object serialize(Object source)
	{
		return serializer.serialize(source);
	}
	protected Object serialize(Object objectToSerialize, SerializationMode serializationMode)
	{
		return serializer.serialize(objectToSerialize, serializationMode);
	}
	protected String getClassName(Object obj)
	{
		return ClassUtil.getClassName(obj);
	}
}
