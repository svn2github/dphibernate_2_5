/**
	Copyright (c) 2008. Digital Primates IT Consulting Group
	http://www.digitalprimates.net
	All rights reserved.
	
	This library is free software; you can redistribute it and/or modify it under the 
	terms of the GNU Lesser General Public License as published by the Free Software 
	Foundation; either version 2.1 of the License.

	This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
	See the GNU Lesser General Public License for more details.

	
	@author: Mike Nimer
	@ignore
 **/

package org.dphibernate.adapters;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dphibernate.operations.AdapterOperation;
import org.dphibernate.persistence.operations.SaveDPProxyOperation;
import org.dphibernate.serialization.IDeserializer;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerConfiguration;
import org.dphibernate.serialization.SimpleSerializationFactory;
import org.dphibernate.serialization.operations.LoadDPProxyBatchOperation;
import org.dphibernate.serialization.operations.LoadDPProxyOperation;
import org.dphibernate.utils.HibernateUtil;

import flex.messaging.Destination;
import flex.messaging.config.ConfigMap;
import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;
import flex.messaging.services.remoting.adapters.JavaAdapter;

@SuppressWarnings("unchecked")
public class RemotingAdapter extends JavaAdapter implements IAdapter
{
	private static final Log log = LogFactory.getLog(RemotingAdapter.class);
	
	protected Destination destination;

	private HashMap<Class<? extends AdapterOperation>,AdapterOperation> operations;
	private ISerializerFactory serializerFactory;
	
	private final AdapterBuilder builder;
	public RemotingAdapter()
	{
		super();
		builder = new AdapterBuilder(this);
	}

	/**
	 * Initialize the adapter properties from the flex services-config.xml file
	 */
	public void initialize(String id, ConfigMap properties)
	{
		super.initialize(id, properties);
		builder.build(properties);
	}
	
	public Object superInvoke(Message message)
	{
		return super.invoke(message);
	}


	/**
	 * Invoke the Object.method() called through FlashRemoting
	 */
	public Object invoke(Message message)
	{
		Object results = null;

		if (message instanceof RemotingMessage)
		{
			// RemotingDestinationControl remotingDestination =
			// (RemotingDestinationControl)this.getControl().getParentControl();//destination;
			RemotingMessage remotingMessage = (RemotingMessage) message;
			
			for (AdapterOperation operation : operations.values())
			{
				if (operation.appliesForMessage(remotingMessage))
				{
					operation.execute(remotingMessage);
				}
			}

			System.out.println("{operation})****************" + remotingMessage.getOperation());
			// Deserialize the incoming object data
			List inArgs = remotingMessage.getParameters();
			if (inArgs != null && inArgs.size() > 0)
			{
				try
				{
					long s1 = new Date().getTime();
					IDeserializer deserializer = getDeserializer();
					Object o = deserializer.translate(this, (RemotingMessage) remotingMessage.clone(), null, null, inArgs);
					long e1 = new Date().getTime();
					System.out.println("{deserialize} " + (e1 - s1));
					// remotingMessage.setBody(body);
				} catch (Exception ex)
				{
					ex.printStackTrace();
					// throw error back to flex
					// todo: replace with custom exception
					RuntimeException re = new RuntimeException(ex.getMessage());
					re.setStackTrace(ex.getStackTrace());
					throw re;
				}
			}

			long s2 = new Date().getTime();
			// invoke the user class.method()
			results = super.invoke(remotingMessage);
			long e2 = new Date().getTime();
			System.out.println("{invoke} " + (e2 - s2));

			// serialize the result out
			try
			{
				long s3 = new Date().getTime();
				ISerializer serializer = serializerFactory.getSerializer(results);
				
				results = serializer.serialize();
				long e3 = new Date().getTime();
				System.out.println("{serialize} " + (e3 - s3));
			} catch (Exception ex)
			{
				ex.printStackTrace();
				// throw error back to flex
				// todo: replace with custom exception
				RuntimeException re = new RuntimeException(ex.getMessage());
				re.setStackTrace(ex.getStackTrace());
				throw re;
			}
		}

		return results;
	}

	protected IDeserializer getDeserializer()
	{
		return serializerFactory.getDeserializer();
	}

	public <T extends AdapterOperation> T getOperation(Class<T> operationClass)
	{
		return (T) operations.get(operationClass);
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
	public ISerializerFactory getSerializerFactory()
	{
		return serializerFactory;
	}

	@Override
	public void setSerializerFactory(ISerializerFactory serializerFactory)
	{
		this.serializerFactory = serializerFactory;
	}
}
