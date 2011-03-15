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

package org.dphibernate.serialization;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dphibernate.context.Context;
import org.dphibernate.core.IEntity;
import org.dphibernate.serialization.annotations.AggressivelyProxy;

import flex.messaging.io.amf.ASObject;

/**
 * Converts a Hibernate Object into an ASObject 
 * 
 * @author mike nimer
 * @author Marty Pitt
 */
@SuppressWarnings("unchecked")
public class HibernateSerializer extends AbstractSerializer implements IPropertySerializer, ProxyPolicy
{
	private static final Log log = LogFactory.getLog(HibernateSerializer.class);
	private PropertyHelper rootPropertyHelper;

	private final Context context;

	private final SerializerConfiguration configuration;
	private final SerializerCache cache;
	
	public HibernateSerializer(SerializerBuilder builder)
	{
		super(builder.getSource());
		rootPropertyHelper = new PropertyHelper(builder.getSource());
		this.context = builder.getContext();
		this.configuration = builder.getConfiguration();
		this.configuration.setPropertySerializer(this);
		this.cache = builder.getCache();
	}

	private boolean canBeAgressivelyProxied(Object objectToSerialize)
	{
		if (!(objectToSerialize instanceof IEntity))
			return false;

		if (isRootObject(objectToSerialize))
		{
			return configuration.getPermitAgressiveProxyingOnRoot();
		}

		return true;
	}

	private boolean hasAnnotation(Object objectToSerialize, Class<? extends Annotation> annotation)
	{
		return Object.class.getAnnotation(annotation) != null;
	}


	private boolean isRootObject(Object objectToSerialize)
	{
		return objectToSerialize == getSource();
	}


	@Override
	/**
	 * ISerializer entry point.
	 * Serializes the source object.
	 * This is the public entry point into serialization
	 */
	public Object serialize()
	{
		return serialize(getSource());
	}

	/**
	 * PropertySerializer serialization for members of getSource(). Called recursively
	 * during serialization
	 */
	@Override
	public Object serialize(Object source)
	{
		return serialize(source, SerializationMode.NORMAL);
	}


	/**
	 * PropertySErializer serialization for members of getSource(). Called recursively
	 * during serialization
	 */
	@Override
	public Object serialize(Object objectToSerialize, SerializationMode serializationMode)
	{
		if (objectToSerialize == null)
		{
			return null;
		}

		Object result = null;

		Object key = getCache().getCacheKey(objectToSerialize);

		if (getCache().contains(key))
		{
			return getCache().get(key);
		}
		
		// Write the cache entry before we start serializing
		// This is to catch recursive self-references within the 
		// object graph, which can lead to a StackOverflow.
		result = writeBean(objectToSerialize, serializationMode,key);
		getCache().store(key, result);
		return result;
	}

	private SerializerCache getCache()
	{
		return cache;
	}
	// TODO : Does this belong here, or on the config?  I think others
	// need to have access to this method
	@Override
	public boolean shouldAggressivelyProxy(Object objectToSerialize, boolean eagerlySerialize)
	{
		if (eagerlySerialize)
			return false;
		if (hasAnnotation(objectToSerialize, AggressivelyProxy.class))
			return true;
		if (!configuration.getUseAggressiveProxying())
			return false;
		return rootPropertyHelper.containsPropertyWithValue(objectToSerialize)
				&& canBeAgressivelyProxied(objectToSerialize);
	}

	private Object writeBean(Object source, SerializationMode serializationMode, Object key)
	{
		ISerializationWriter writer = getWriter(source, serializationMode);
		// To help with beans with self-references (common in parent-child relationships)
		// we get a stub and cache it before the source is actually serialized.
		Object stub = writer.createStubValue(source);
		getCache().store(key, stub);
		writer.populateStub(source,stub);
		return stub;
	}

	private ISerializationWriter getWriter(Object source, SerializationMode serializationMode)
	{
		return context.getWriterFactory().getWriter(source,getCache(),serializationMode,configuration);
	}

	@Override
	public SerializerConfiguration getConfiguration()
	{
		return configuration;
	}

}
