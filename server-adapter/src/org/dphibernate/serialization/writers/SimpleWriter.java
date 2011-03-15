package org.dphibernate.serialization.writers;

import org.dphibernate.serialization.ISerializationWriter;

/**
 * A null writer - performs no serialization of the provided type
 * Utlimately, serialization is left to BlazeDS to manage.
 * @author Marty Pitt
 *
 */
public class SimpleWriter implements ISerializationWriter<Object,Object>
{

	@Override
	public Object createStubValue(Object source)
	{
		return source;
	}

	@Override
	public void populateStub(Object source, Object stub)
	{
	}

}
