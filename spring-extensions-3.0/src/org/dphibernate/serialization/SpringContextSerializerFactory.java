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

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import flex.messaging.FlexContext;

/**
 * Returns a serializer / deserializer from a Spring context.
 * 
 */
public class SpringContextSerializerFactory implements ISerializerFactory
{
	@Resource
	private SessionFactory sessionFactory;
	
	private SerializerConfiguration defaultConfiguration;
	
	private ContextReference serializerContextReference;

	private ContextReference deserializerContextReference;

	@Override
	public ISerializer getSerializer(Object source)
	{
		return getSerializer(source,false);
	}
	@Override
	public ISerializer getSerializer(Object source,boolean useAggressiveSerialization)
	{
		ContextReference reference = getSerializerContextReference();
		ISerializer serializer = (ISerializer) reference.getContext().getBean(reference.getBeanName(),new Object[]{source,useAggressiveSerialization});
		serializer.configure(defaultConfiguration);
		return serializer;
	}
	private ContextReference getSerializerContextReference() {
		if (serializerContextReference != null)
			return serializerContextReference;
		
		serializerContextReference = getContextReference(ISerializer.class);
		if (serializerContextReference == null)
		{
			throw new RuntimeException("No serializer is configured in any discovered Spring context.  Ensure there is exactly one dpHibernate ISerializer defined");
		}
		return serializerContextReference;
	}
	@Override
	public IDeserializer getDeserializer()
	{
		ContextReference reference = getDeserializerContextReference();
		IDeserializer deserializer = (IDeserializer) reference.getContext().getBean(reference.getBeanName());
		if (deserializer == null)
		{
			deserializer = new HibernateDeserializer();
		}
		return deserializer;
	}
	private ContextReference getDeserializerContextReference() {
		if (deserializerContextReference != null)
			return deserializerContextReference;
		
		deserializerContextReference = getContextReference(IDeserializer.class);
		if (deserializerContextReference == null)
		{
			throw new RuntimeException("No deserializer is configured in any discovered Spring context.  Ensure there is exactly one dpHibernate IDeserializer defined");
		}
		return deserializerContextReference;
	}
	private ContextReference getContextReference(Class beanClass)
	{
		HttpServletRequest request = FlexContext.getHttpRequest();
		// Try to find the context for the correct DipsatcherServlet.
		WebApplicationContext context;
		String beanName;
		context = RequestContextUtils.getWebApplicationContext(request);
		if (context != null)
		{
			beanName = getUUniqueBeanName(context, beanClass);
			if (beanName != null)
			{
				return new ContextReference(beanName, context);
			}
		}
		// Get the root instead.
		ServletContext servletContext = FlexContext.getServletContext();
		context= WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		if (context != null)
		{
			beanName = getUUniqueBeanName(context,beanClass);
			if (beanName != null)
			{
				return new ContextReference(beanName, context);
			}
		}
		return null;
	}
	String getUUniqueBeanName(ApplicationContext context,Class beanClass)
	{
		String[] beanNames = context.getBeanNamesForType(beanClass);
		if (beanNames.length == 0)
		{
			return null;
		}
		if (beanNames.length > 1)
		{
			throw new RuntimeException("More than one Serializer is configured in the Spring context.  Ensure exactly one one ISerializer instance is declared");
		}
		return beanNames[0];
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	@Override
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
	@Override
	public void setDefaultConfiguration(SerializerConfiguration configuration)
	{
		this.defaultConfiguration = configuration;
	}
}
