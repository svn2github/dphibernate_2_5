package org.dphibernate.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hibernate.transform.PassThroughResultTransformer;
import org.hibernate.transform.ResultTransformer;

public class ResultTransformerUtil
{

	public static final ResultTransformer PASS_THROUGH_RESULT_TRANSFORMER = getResultTransformer();

	private static ResultTransformer getResultTransformer()
	{
		if (hasSingletonField())
		{
			return getResultTransformerSingleton();
		} else {
			return createNewResultTransformer();
		}
	}
	private static ResultTransformer createNewResultTransformer()
	{
		try
		{
			return PassThroughResultTransformer.class.newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception thrown when instantiating new PassThroughResultTransformer.  This approach is used versions of hibernate 3.2 or earlier.",e);
		}
	}
	private static ResultTransformer getResultTransformerSingleton()
	{
		Field singletonField = getSingletonField();
		ResultTransformer resultTransformer;
		try
		{
			
			resultTransformer = (ResultTransformer) singletonField.get(null);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception thrown when referencing PassThroughResultTransformer.INSTANCE  This approach is used versions of hibernate 3.3 or later.",e);
		}
		return resultTransformer;
	}
	private static boolean hasSingletonField()
	{
		return getSingletonField() != null;
	}
	private static Field getSingletonField()
	{
		Class<PassThroughResultTransformer> resultTransformerClass = PassThroughResultTransformer.class;
		Field singletonField;
		try
		{
			singletonField = resultTransformerClass.getField("INSTANCE");
		} catch (Exception e)
		{
			// An exception is thrown in versions of Hibernate earlier than 3.3
			// because the method does not exist
			// Return null
			return null;
		}
		return singletonField;
	}
}
