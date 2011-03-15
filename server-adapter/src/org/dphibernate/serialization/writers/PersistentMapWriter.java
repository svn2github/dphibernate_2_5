package org.dphibernate.serialization.writers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializationWriter;
import org.dphibernate.serialization.ISerializer;
import org.hibernate.collection.PersistentMap;

public class PersistentMapWriter extends AbstractSerialziationWriter implements ISerializationWriter<PersistentMap,HashMap>
{

	public PersistentMapWriter(IPropertySerializer serializer)
	{
		super(serializer);
	}
	@Override
	public HashMap createStubValue(PersistentMap source)
	{
		return new HashMap();
	}
	@Override
	public void populateStub(PersistentMap source, HashMap stub)
	{
		if (source.wasInitialized())
		{
			Set keys = source.keySet();
			Iterator keyItr = keys.iterator();
			while (keyItr.hasNext())
			{
				Object mapKey = keyItr.next();
				Object value = source.get(mapKey);
				serialize(value);
				stub.put(mapKey, value);
			}
		} else
		{
			throw new RuntimeException("Lazy loaded maps not implimented yet.");
		}
	}

}
