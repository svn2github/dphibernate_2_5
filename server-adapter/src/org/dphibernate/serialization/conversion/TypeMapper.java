package org.dphibernate.serialization.conversion;

import java.util.ArrayList;
import java.util.List;


public class TypeMapper {
//	Mapper mapper;
	List<IConverter> converters;
	public TypeMapper()
	{
//		this(new DozerBeanMapper());
		converters = new ArrayList<IConverter>();
		converters.add(new DateCalendarConverter());
		converters.add(new BooleanConverter());
		converters.add(new NumberConverter());

	}
	/*
	public TypeMapper(Mapper mapper)
	{
	
//		this.mapper = mapper;
	}
	*/
	
	public Object convert(Object source,Class<?> targetClass)
	{
		for (IConverter converter : converters)
		{
			if (converter.canConvert(source.getClass(),targetClass))
			{
				return converter.convert(source,targetClass);
			}
		}
		throw new RuntimeException("No converter found");

	}
	public boolean canConvert(Object source,Class<?> targetClass)
	{
		for (IConverter converter : converters)
		{
			if (converter.canConvert(source.getClass(),targetClass))
			{
				return true;
			}
		}
		return false;
	}
}
