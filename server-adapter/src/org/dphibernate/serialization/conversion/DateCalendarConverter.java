package org.dphibernate.serialization.conversion;

import java.util.Calendar;
import java.util.Date;

public class DateCalendarConverter implements IConverter {

	@Override
	public boolean canConvert(Class<?> class1, Class<?> targetClass) {
		if (Calendar.class.isAssignableFrom(class1) && Date.class.isAssignableFrom(targetClass)) return true;
		if (Date.class.isAssignableFrom(class1) && Calendar.class.isAssignableFrom(targetClass)) return true;
		return false;
	}

	@Override
	public Object convert(Object source, Class<?> targetClass) {
		if (source instanceof Date && targetClass.isAssignableFrom(Calendar.class))
		{
			Calendar result = Calendar.getInstance();
			result.setTime((Date) source);
			return result;
		}
		if (source instanceof Calendar && targetClass.isAssignableFrom(Date.class))
		{
			return ((Calendar) source).getTime();
		}
		throw new RuntimeException("Cannot perform conversion");
	}

}
