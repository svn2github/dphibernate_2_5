package org.dphibernate.serialization.conversion;

public interface IConverter {

	boolean canConvert(Class<?> class1, Class<?> targetClass);

	Object convert(Object source, Class<?> targetClass);

}
