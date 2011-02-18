package org.dphibernate.serialization;

public abstract class AbstractSerializer implements ISerializer
{
	private final Object source;
	public AbstractSerializer(Object source)
	{
		this.source = source;
	}
	public Object getSource()
	{
		return source;
	}
	
}
