package org.dphibernate.serialization;

public enum SerializationMode
{
	AGGRESSIVELY_PROXY,NORMAL,EAGERLY_SERIALIZE;
	
	public boolean eagerlySerialize()
	{
		return this == EAGERLY_SERIALIZE;
	}
	
	
}
