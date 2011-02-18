package org.dphibernate.adapters;

import java.util.HashMap;

import org.dphibernate.operations.AdapterOperation;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.serialization.ISerializerFactory;

import flex.messaging.config.ConfigMap;
import flex.messaging.messages.Message;
import flex.messaging.services.messaging.adapters.ActionScriptAdapter;

public class MessagingAdapter extends ActionScriptAdapter implements IAdapter
{
	private ISerializerFactory serializerFactory;
	private HashMap<Class<? extends AdapterOperation>,AdapterOperation> operations;
	private final AdapterBuilder builder;
	
	public MessagingAdapter()
	{
		this.builder = new AdapterBuilder(this);
	}
	
	/**
	 * Initialize the adapter properties from the flex services-config.xml file
	 */
	@Override
	public void initialize(String id, ConfigMap properties)
	{
		super.initialize(id, properties);
		builder.build(properties);
	}
	
	@Override
	/**
     * Handle a data message intended for this adapter.
     */
    public Object invoke(Message message)
    {
        ISerializer serializer = serializerFactory.getSerializer(message.getBody(),true);
        
        Object translatedBody = serializer.serialize();
        message.setBody(translatedBody);
        return super.invoke(message);
    }
	@Override
	public ISerializerFactory getSerializerFactory()
	{
		return serializerFactory;
	}
	@Override
	public void putOperation(AdapterOperation operation)
	{
		if (operations == null)
		{
			operations = new HashMap<Class<? extends AdapterOperation>, AdapterOperation>();
		}
		operations.put(operation.getClass(), operation);

		
	}
	@Override
	public void setSerializerFactory(ISerializerFactory serializerFactory)
	{
		this.serializerFactory = serializerFactory;
	}
	
}
