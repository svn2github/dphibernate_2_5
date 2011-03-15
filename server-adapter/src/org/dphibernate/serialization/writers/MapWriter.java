package org.dphibernate.serialization.writers;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializationWriter;
import org.dphibernate.serialization.ISerializer;

import flex.messaging.io.amf.ASObject;

public class MapWriter extends AbstractSerialziationWriter implements ISerializationWriter<Map,ASObject>
{

	public MapWriter(IPropertySerializer serializer)
	{
		super(serializer);
	}

	@Override
	public ASObject createStubValue(Map source)
	{
		if (source instanceof ASObject)
		{
			return (ASObject) source;
		}

		ASObject asObj = new ASObject();
		asObj.setType(getClassName(source));
		
		return asObj;
	}

	@Override
	public void populateStub(Map source, ASObject stub)
	{
		if (source instanceof ASObject)
			return;

		Set keys = source.keySet();
		Iterator keysItr = keys.iterator();
		for (Object key:source.keySet())
		{
			Object value = source.get(key);
			stub.put(key, serialize(value));
		}
	}
}
