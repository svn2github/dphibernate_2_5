package org.dphibernate.serialization;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dphibernate.serialization.annotations.AggressivelyProxy;
import org.dphibernate.serialization.annotations.EagerlySerialize;
import org.dphibernate.serialization.annotations.NeverSerialize;

public class PropertyHelper
{
	private static final List<String> EXCLUDED_PROPERTY_NAMES = Arrays.asList("handler", "class", "hibernateLazyInitializer", "proxyInitialized");
	private final Object source;
	private Map<String, SerializableProperty> properties;


	public PropertyHelper(Object source)
	{
		this.source = source;
	}


	public Object getSource()
	{
		return source;
	}


	/**
	 * Returns a list of properties suitable for serialization. Invalid
	 * properties are exclluded
	 * 
	 * @return
	 */
	public Collection<SerializableProperty> getProperties()
	{
		if (properties == null)
		{
			initializePropertyList();
		}
		return properties.values();
	}


	private void initializePropertyList()
	{
		Map<String, SerializableProperty> result = new HashMap<String, SerializableProperty>();
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
			for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors())
			{
				String propName = pd.getName();
				Method readMethod = pd.getReadMethod();
				if (readMethod == null || excludeMethod(readMethod) || excludeProperty(propName))
					continue;
				Object val = readMethod.invoke(source, null);
				SerializationMode serializationMode = getSerializationMode(readMethod);
				SerializableProperty property = new SerializableProperty(propName, val, serializationMode);
				result.put(propName, property);
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		properties = result;
	}


	public Object getPropertyValue(String name)
	{
		return properties.get(name).getValue();
	}


	public boolean containsPropertyWithName(String name)
	{
		for (SerializableProperty property : getProperties())
		{
			if (property.getName().equals(name))
				return true;
		}
		return false;
	}


	public boolean containsPropertyWithValue(Object value)
	{
		for (SerializableProperty property : getProperties())
		{
			if (property.getValue() == null)
				continue;

			if (property.getValue().equals(value))
				return true;
		}
		return false;
	}


	private SerializationMode getSerializationMode(Method readMethod)
	{
		if (methodHasAnnotation(readMethod, AggressivelyProxy.class))
			return SerializationMode.AGGRESSIVELY_PROXY;
		if (methodHasAnnotation(readMethod, EagerlySerialize.class))
			return SerializationMode.EAGERLY_SERIALIZE;
		return SerializationMode.NORMAL;
	}


	private boolean excludeProperty(String propName)
	{
		return EXCLUDED_PROPERTY_NAMES.contains(propName);
	}


	private boolean excludeMethod(Method readMethod)
	{
		if (readMethod == null)
			return true;
		if (methodHasAnnotation(readMethod, NeverSerialize.class))
			return true;
		return false;
	}


	private boolean methodHasAnnotation(Method readMethod, Class<? extends Annotation> annotation)
	{
		return readMethod.getAnnotation(annotation) != null;
	}


	public void removeProperty(String propertyName)
	{
		properties.remove(propertyName);
	}

}
