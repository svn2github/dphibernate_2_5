package org.dphibernate.proxy;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Field;

import org.apache.commons.lang.ClassUtils;
import org.dphibernate.utils.ClassUtil;
import org.hibernate.SessionFactory;
import org.hibernate.property.Getter;
import org.hibernate.util.PropertiesHelper;
import org.hibernate.util.ReflectHelper;

public class HibernateIdentityProvider implements EntityIdentityProvider
{
	private final SessionFactory sessionFactory;


	public HibernateIdentityProvider(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}


	@Override
	public Object getId(Object entity)
	{
		try
		{
			Class<?> entityClass = entity.getClass();
			String identifierPropertyName = sessionFactory.getClassMetadata(entityClass).getIdentifierPropertyName();
			Getter getter = ReflectHelper.getGetter(entityClass, identifierPropertyName);
			return getter.get(entity);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
