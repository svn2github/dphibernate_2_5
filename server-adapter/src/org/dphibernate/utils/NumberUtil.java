package org.dphibernate.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtil {
	
	/**
	 * Converts a Serializable Object to the specified Serializable Number Type
	 * @param numberObject
	 * @param toType
	 * @return
	 */
	public static Serializable convertSerializableToNumberType(Serializable numberObject, Class<?> toType)
	{
		Class<?> fromClass = numberObject.getClass();
		
		// If from class is the same as the to class, return the object.
		if(fromClass.equals(toType))
		{
			return numberObject;
		}
		
		Serializable value = null;
		
		if(java.lang.Long.class.equals(toType))
		{
			value = getValueAsLong(numberObject);
		}
		else if(java.lang.Integer.class.equals(toType))
		{
			value = getValueAsInteger(numberObject);
		}
		else if(java.lang.Double.class.equals(toType))
		{
			value = getValueAsDouble(numberObject);
		}
		else if(java.lang.Float.class.equals(toType))
		{
			value = getValueAsFloat(numberObject);
		}
		else if(java.math.BigDecimal.class.equals(toType))
		{
			value = getValueAsBigDecimal(numberObject);
		}
		else if(java.math.BigInteger.class.equals(toType))
		{
			value = getValueAsBigInteger(numberObject);
		}
		else if(java.lang.Short.class.equals(toType))
		{
			value = getValueAsShort(numberObject);
		}
		else
		{
			value = numberObject;
		}
		
		return value;
	}
	
	/**
	 * Converts an Object to Long.
	 * @param object
	 * @return
	 */
	public static Long getValueAsLong(Object object) 
	{
		Class<?> objectClass = object.getClass();
		Long value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.lang.Long.class))
			{
				value = (Long) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new Long(temp.longValue());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new Long(temp);
		}
		else
		{
			value = (Long) object;
		}
		return value;
	}
	
	/**
	 * Converts an Object to Integer
	 * @param object
	 * @return
	 */
	public static Integer getValueAsInteger(Object object) 
	{
		Class<?> objectClass = object.getClass();
		Integer value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.lang.Integer.class))
			{
				value = (Integer) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new Integer(temp.intValue());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new Integer(temp);
		}
		else
		{
			value = (Integer) object;
		}
		return value;
	}
	
	/**
	 * Converts an Object to Double
	 * @param object
	 * @return
	 */
	public static Double getValueAsDouble(Object object) 
	{
		Class<?> objectClass = object.getClass();
		Double value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.lang.Double.class))
			{
				value = (Double) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new Double(temp.doubleValue());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new Double(temp);
		}
		else
		{
			value = (Double) object;
		}
		return value;
	}
	
	/**
	 * Converts an Object to Float
	 * @param object
	 * @return
	 */
	public static Float getValueAsFloat(Object object) 
	{
		Class<?> objectClass = object.getClass();
		Float value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.lang.Float.class))
			{
				value = (Float) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new Float(temp.floatValue());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new Float(temp);
		}
		else
		{
			value = (Float) object;
		}
		return value;
	}
	
	/**
	 * Converts an Object to BigDecimal
	 * @param object
	 * @return
	 */
	public static BigDecimal getValueAsBigDecimal(Object object) 
	{
		Class<?> objectClass = object.getClass();
		BigDecimal value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.math.BigDecimal.class))
			{
				value = (BigDecimal) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new BigDecimal(temp.doubleValue());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new BigDecimal(temp);
		}
		else
		{
			value = (BigDecimal) object;
		}
		return value;
	}
	
	/**
	 * Converts an Object to BigInteger
	 * @param object
	 * @return
	 */
	public static BigInteger getValueAsBigInteger(Object object) 
	{
		Class<?> objectClass = object.getClass();
		BigInteger value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.math.BigInteger.class))
			{
				value = (BigInteger) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new BigInteger(temp.toString());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new BigInteger(temp);
		}
		else
		{
			value = (BigInteger) object;
		}
		return value;
	}
	
	/**
	 * Converts an Object to Short
	 * @param object
	 * @return
	 */
	public static Short getValueAsShort(Object object) 
	{
		Class<?> objectClass = object.getClass();
		Short value = null;
		if(object instanceof Number)
		{
			if(objectClass.equals(java.lang.Short.class))
			{
				value = (Short) object;
			}
			else 
			{
				Number temp = (Number) object;
				value = new Short(temp.shortValue());
			}
		}
		else if (objectClass.equals(java.lang.String.class))
		{
			String temp = (String) object;
			value =  new Short(temp);
		}
		else
		{
			value = (Short) object;
		}
		return value;
	}
}
