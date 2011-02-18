package org.dphibernate.serialization;

import org.dphibernate.serialization.IDeserializer;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerConfiguration;
import org.hibernate.SessionFactory;


public class MockSerializerFactory implements ISerializerFactory
{

	@Override
	public IDeserializer getDeserializer()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ISerializer getSerializer(Object source)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ISerializer getSerializer(Object source, boolean useAggressiveSerialization)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SessionFactory getSessionFactory()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setDefaultConfiguration(SerializerConfiguration configuration)
	{
		// TODO Auto-generated method stub
		
	}

}
