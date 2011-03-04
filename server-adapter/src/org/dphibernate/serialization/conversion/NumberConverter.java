package org.dphibernate.serialization.conversion;

import java.math.BigDecimal;

public class NumberConverter implements IConverter
{
	@Override
	public boolean canConvert(Class<?> class1, Class<?> targetClass)
	{
		if (isPrimitiveNumber(class1) && isReferenceNumber(targetClass))
			return true;
		if (isReferenceNumber(class1) && isPrimitiveNumber(targetClass))
			return true;
		if (isPrimitiveNumber(class1) && isPrimitiveNumber(targetClass))
			return true;
		if (isReferenceNumber(class1) && isReferenceNumber(targetClass))
			return true;
		return false;
	}


	private boolean isReferenceNumber(Class<?> targetClass)
	{
		return (targetClass.isAssignableFrom(Integer.class) 
				|| targetClass.isAssignableFrom(Long.class)
				|| targetClass.isAssignableFrom(BigDecimal.class)
				|| targetClass.isAssignableFrom(Double.class));
	}


	private boolean isPrimitiveNumber(Class<?> targetClass)
	{
		return (targetClass.isAssignableFrom(int.class) 
				|| targetClass.isAssignableFrom(long.class)
				|| targetClass.isAssignableFrom(double.class));

	}


	@Override
	public Object convert(Object source, Class<?> targetClass)
	{
		if (source.equals(Double.NaN))
		{
			if (targetClass.isAssignableFrom(Double.class) || targetClass.isAssignableFrom(double.class))
			{
				return source;
			}
			if (isPrimitiveNumber(targetClass))
			{
				return 0;
			} else {
				return null;
			}
		}
		if (targetClass.isAssignableFrom(Integer.class) || targetClass.isAssignableFrom(int.class))
		{
			return (Integer) source;
		}
		if (targetClass.isAssignableFrom(Double.class) || targetClass.isAssignableFrom(double.class))
		{
			return (Double) source;
		}
		if (targetClass.isAssignableFrom(Long.class) || targetClass.isAssignableFrom(long.class))
		{
			return Long.parseLong(source.toString());
//			return (Long) source;
		}
		if (targetClass.isAssignableFrom(BigDecimal.class))
		{
			return new BigDecimal(source.toString());
		}
		return targetClass.cast(source);
	}
}
