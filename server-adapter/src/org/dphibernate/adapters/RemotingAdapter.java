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
import org.dphibernate.serialization.IDeserializer;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.serialization.ISerializerFactory;
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

	private HashMap<Class<? extends AdapterOperation>, AdapterOperation> operations;
	private ISerializerFactory serializerFactory;

	private final AdapterBuilder builder;


	public RemotingAdapter()
	{
		super();
		builder = new AdapterBuilder(this);
	}
	
	public Object superInvoke(Message message)
	{
		return super.invoke(message);
	}

	/**
	 * Checks the remotingMessage for any dpHibernate entities, and expands them
	 * using a deserializer.
	 * 
	 * @param remotingMessage
	 */
	private void deserializeMessage(RemotingMessage remotingMessage)
	{
		List inArgs = remotingMessage.getParameters();
		if (inArgs != null && inArgs.size() > 0)
		{
			try
			{
				StopWatch sw = new StopWatch("deserialize");

				IDeserializer deserializer = getDeserializer();
				Object o = deserializer.translate(this, (RemotingMessage) remotingMessage.clone(), null, null, inArgs);

				sw.stopAndLog();
			} catch (Exception ex)
			{
				ex.printStackTrace();
				RuntimeException re = new RuntimeException(ex.getMessage());
				re.setStackTrace(ex.getStackTrace());
				throw re;
			}
		}
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
	public ISerializerFactory getSerializerFactory()
	{
		return serializerFactory;
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


	/**
	 * Invoke the Object.method() called through FlashRemoting
	 */
	@Override
	public Object invoke(Message message)
	{
		Object results = null;

		if (message instanceof RemotingMessage)
		{
			RemotingMessage remotingMessage = (RemotingMessage) message;

			invokeAdapterOperations(remotingMessage);

			deserializeMessage(remotingMessage);

			StopWatch sw = new StopWatch("invoke " + remotingMessage.getOperation());
			results = super.invoke(remotingMessage);
			sw.stopAndLog();

			results = serializeResults(results);
		}

		return results;
	}


	private void invokeAdapterOperations(RemotingMessage remotingMessage)
	{
		for (AdapterOperation operation : operations.values())
		{
			if (operation.appliesForMessage(remotingMessage))
			{
				operation.execute(remotingMessage);
			}
		}
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


	/**
	 * Invokes the dpHibernate serializer to replace entities with lazy-loading
	 * equvilants
	 * 
	 * @param results
	 * @return
	 */
	private Object serializeResults(Object results)
	{
		try
		{
			StopWatch sw = new StopWatch("serialize");

			ISerializer serializer = serializerFactory.createSerializerFor(results).build();
			results = serializer.serialize();

			sw.stopAndLog();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			// throw error back to flex
			// todo: replace with custom exception
			RuntimeException re = new RuntimeException(ex.getMessage());
			re.setStackTrace(ex.getStackTrace());
			throw re;
		}
		return results;
	}


	@Override
	public void setSerializerFactory(ISerializerFactory serializerFactory)
	{
		this.serializerFactory = serializerFactory;
	}
}

class StopWatch
{
	private final Date startDate;
	private final String description;


	public StopWatch(String description)
	{
		this.description = description;
		startDate = new Date();
	}


	public void stopAndLog()
	{
		Date endDate = new Date();
		System.out.println("dpHibernate:  " + description + " completed in " + (endDate.getTime() - startDate.getTime()) + "ms");
	}
}
