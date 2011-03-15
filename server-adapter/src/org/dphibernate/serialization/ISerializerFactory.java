package org.dphibernate.serialization;


public interface ISerializerFactory
{
	public SerializerBuilder createSerializerFor(Object source);
	
	public IDeserializer getDeserializer();
}