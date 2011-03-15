package org.dphibernate.utils;

import org.hibernate.proxy.HibernateProxy;

import flex.messaging.io.amf.ASObject;

public class ClassUtil
{
	public static String getClassName(Object obj)
	{
		if (obj instanceof ASObject)
		{
			return ((ASObject) obj).getType();
		} else if (obj instanceof HibernateProxy)
		{
			return ((HibernateProxy) obj).getHibernateLazyInitializer().getPersistentClass().getName().toString();
		} else
		{
			return obj.getClass().getName();
		}
	}
}
