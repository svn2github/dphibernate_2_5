package org.dphibernate.serialization.writers;

import java.util.Collection;

import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializationWriter;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.PropertyHelper;
import org.dphibernate.serialization.SerializableProperty;
import org.dphibernate.serialization.SerializerCache;

import flex.messaging.io.amf.ASObject;


public abstract class AbstractObjectWriter<T,K> extends AbstractSerialziationWriter implements ISerializationWriter<T,K>
{
	private final ISerializerFactory serializerFactory;
	private final SerializerCache cache;


	public AbstractObjectWriter(SerializerCache cache, ISerializerFactory serializerFactory, IPropertySerializer serializer)
	{
		super(serializer);
		this.cache = cache;
		this.serializerFactory = serializerFactory;
	}

	@Override 
	public K createStubValue(T source)
	{
		ASObject asObject = getDefaultASObject(source);
		return (K) asObject;
	}

	@Override
	public void populateStub(T source,K stub)
	{
		ASObject asObject = (ASObject) stub;
		PropertyHelper helper = new PropertyHelper(source);
		
		for (SerializableProperty property : getPropertiesToSerialize(helper))
		{
			Object serializedValue;
			if (property.shouldAggressivelyProxy())
			{
				ISerializer aggressiveSerializer = serializerFactory.createSerializerFor(source)
					.usingCache(cache)
					.thatAggressivelyProxies()
					.and()
					.permitsAggressiveProxyingOnRoot()
					.build();
				serializedValue = aggressiveSerializer.serialize();
			} else
			{
				serializedValue = serialize(property.getValue(), property.getSerializationMode());
			}
			asObject.put(property.getName(),serializedValue);
		}
	}


	/**
	 * Returns a collection of properties to serialize.
	 * Note, the PropertyHelper excludes most types that should not
	 * be serialized.  Generally, this method should only be overridden in advanced
	 * scenarios where you need to apply additional filtering on the properties to serialize. 
	 */
	protected Collection<SerializableProperty> getPropertiesToSerialize(PropertyHelper helper)
	{
		return helper.getProperties();
	}


	protected ASObject getDefaultASObject(T source)
	{
		ASObject asObject = new ASObject();
		asObject.setType(getClassName(source));
		return asObject;
	}
}
