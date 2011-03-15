package org.dphibernate.serialization.writers;

import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerCache;

import flex.messaging.io.amf.ASObject;

public class ObjectWriter extends AbstractObjectWriter<Object,ASObject>
{

	public ObjectWriter(SerializerCache cache, ISerializerFactory serializerFactory, IPropertySerializer serializer)
	{
		super(cache, serializerFactory, serializer);
	}

}
