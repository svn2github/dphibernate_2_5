package org.dphibernate.serialization.writers;

import org.dphibernate.serialization.ISerializationWriter;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import flex.messaging.io.amf.ASObject;

public class DpHibernateProxyWriter implements ISerializationWriter<Object,ASObject>
{

	@Override
	public ASObject createStubValue(Object source)
	{
		return new ASObject();
	}

	@Override
	public void populateStub(Object source, ASObject stub)
	{
		throw new NotImplementedException();
		// TODO Auto-generated method stub
		
	}

}
