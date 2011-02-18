package org.dphibernate.serialization;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.dphibernate.serialization.annotations.NeverSerialize;


public class PropertyHelper
{

	public static Map<String,Object> getProperties(Object obj)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		BeanInfo beanInfo = getBeanInfo(obj);
		for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors())
		{
			String propName = pd.getName();
			Method readMethod = pd.getReadMethod();
			if (excludeMethod(readMethod,propName))
				continue;
			try
			{
				Object val = readMethod.invoke(obj, null);
				result.put(propName, val);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}
	private static BeanInfo getBeanInfo(Object obj)
	{
		try
		{
			BeanInfo info = Introspector.getBeanInfo(obj.getClass());
			return info;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private static boolean excludeMethod(Method readMethod, String propName)
	{
		if (readMethod == null)
			return true;
		if (propertyNameIsExcluded(propName))
			return true;
		if (methodHasAnnotation(readMethod, NeverSerialize.class))
			return true;
		return false;
	}
	public static boolean propertyNameIsExcluded(String propName)
	{
		// Note - exclude proxyInitialized because we don't serialzie the value present on the class -- it's up to us to decide
		return propName.equals("handler") || propName.equals("class") || propName.equals("hibernateLazyInitializer") || propName.equals("proxyInitialized");
	}
	public static boolean methodHasAnnotation(Method readMethod, Class<? extends Annotation> annotation)
	{
		return readMethod.getAnnotation(annotation) != null;
	}

}
