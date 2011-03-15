package org.dphibernate.serialization;

import org.hibernate.dialect.Dialect;
import org.hibernate.impl.SessionFactoryImpl;

/**
 * Class which holds the config for serializers.
 * Will add properties here.
 * @author Marty Pitt
 *
 */
public class SerializerConfiguration implements ProxyPolicy
{
	private SerializationMode mode;
	private int pageSize = -1;
	private boolean permitAgressiveProxyingOnRoot;
	private boolean useAggressiveProxying;

	// Not really sure how to handle this yet ... need to decide
	// where to setup / pass the propertySerializer
	private IPropertySerializer propertySerializer;
	public SerializerConfiguration()
	{
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	public void setMode(SerializationMode mode)
	{
		this.mode = mode;
	}

	public SerializationMode getMode()
	{
		return mode;
	}

	// MP : Should this be renamed to simply : shouldProxy()? 
	// Also, I think the eagerlySerialize param is reduntant here,
	// as we already know the serializationMode
	@Override
	public boolean shouldAggressivelyProxy(Object source, boolean eagerlySerialize)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void setPermitAgressiveProxyingOnRoot(boolean permitAgressiveProxyingOnRoot)
	{
		this.permitAgressiveProxyingOnRoot = permitAgressiveProxyingOnRoot;
	}

	public boolean getPermitAgressiveProxyingOnRoot()
	{
		return permitAgressiveProxyingOnRoot;
	}

	public void setUseAggressiveProxying(boolean useAggressiveProxying)
	{
		this.useAggressiveProxying = useAggressiveProxying;
	}

	public boolean getUseAggressiveProxying()
	{
		return useAggressiveProxying;
	}
	
	public SerializerConfiguration clone()
	{
		SerializerConfiguration clone = new SerializerConfiguration();
		clone.mode = this.mode;
		clone.pageSize = this.pageSize;
		clone.permitAgressiveProxyingOnRoot = this.permitAgressiveProxyingOnRoot;
		clone.useAggressiveProxying = this.useAggressiveProxying;
		clone.propertySerializer = propertySerializer;
		return clone;
	}

	public void setPropertySerializer(IPropertySerializer propertySerializer)
	{
		this.propertySerializer = propertySerializer;
	}

	public IPropertySerializer getPropertySerializer()
	{
		return propertySerializer;
	}
	
}
