package org.dphibernate.serialization;

/**
 * Class which holds the config for serializers.
 * Will add properties here.
 * @author Marty Pitt
 *
 */
public class SerializerConfiguration
{
	private final int pageSize;

	public SerializerConfiguration(int pageSize)
	{
		this.pageSize = pageSize;
		
	}

	public int getPageSize()
	{
		return pageSize;
	}
}
