package org.dphibernate.serialization.conversion;


public class BooleanConverter implements IConverter
{

	@Override
	public boolean canConvert(Class<?> class1, Class<?> targetClass)
	{
		if (String.class.isAssignableFrom(class1) && (Boolean.class.isAssignableFrom(targetClass) || boolean.class.isAssignableFrom(targetClass))) 
			{
			return true;
			}
		if (boolean.class.isAssignableFrom(class1) && Boolean.class.isAssignableFrom(targetClass))
			return true;
		if (Boolean.class.isAssignableFrom(class1) && boolean.class.isAssignableFrom(targetClass))
			return true;
		return false;
	}


	@Override
	public Object convert(Object source, Class<?> targetClass)
	{
		return Boolean.parseBoolean(source.toString());
	}

}
