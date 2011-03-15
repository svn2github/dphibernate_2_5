package org.dphibernate.utils;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.Type;

public class EntityUtil {
	
	/**
	 * Returns the class of the entity's Id using hibernate's SessionFactory.
	 * @param entity
	 * @param sessionFactory
	 * @return
	 */
	public static Class<?> getEntityIdClass(Class<?> entity, SessionFactory sessionFactory)
	{
		ClassMetadata daoClassMetadata = sessionFactory.getClassMetadata(entity);
		Type idHibernateType = daoClassMetadata.getIdentifierType();
		Class<?> entityIdClass = idHibernateType.getReturnedClass();
		return entityIdClass;
	}
	public static boolean isLazyProxy(Object obj)
	{
		return obj instanceof HibernateProxy && (((HibernateProxy) obj).getHibernateLazyInitializer().isUninitialized());
	}
}
