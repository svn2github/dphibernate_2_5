package org.dphibernate.serialization.writers;

import java.util.ArrayList;
import java.util.List;

import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializationWriter;

import flex.messaging.io.amf.ASObject;

/*
 * Returns Object[] (as opposed to ASObject[])
 * as Object[] serializes as ASObject[], which cannot be added to
 * ASObject[]
 */
public class ArrayWriter extends AbstractSerialziationWriter implements ISerializationWriter<Object[],Object[]>
{

	public ArrayWriter(IPropertySerializer serializer)
	{
		super(serializer);
	}

	@Override
	public Object[] createStubValue(Object[] source)
	{
		return new Object[source.length];
	}

	@Override
	public void populateStub(Object[] source, Object[] stub)
	{
		for (int i = 0; i < source.length; i++)
		{
			Object member = source[i];
			stub[i] = (ASObject) serialize(member);
		}
	}
}
