package org.dphibernate.serialization;

import java.util.UUID;

import org.dphibernate.core.HibernateProxyConstants;
import org.dphibernate.utils.ClassUtil;

import flex.messaging.io.amf.ASObject;
/**
 * Subclass of ASObject to provide contstructors
 * meaningful in the dpHibernate context
 * @author Marty Pitt
 *
 */
public class DPHibernateASObject extends ASObject
{

	public static ASObject initializedEntity(Object source, Object key)
	{
		return entity(source, key, true);
	}
	public static ASObject uninitializedEntity(Object source, Object key)
	{
		return entity(source, key, false);
	}
	public static ASObject entity(Object source, Object key, boolean isInitialized)
	{
		ASObject as = new ASObject();
		as.setType(ClassUtil.getClassName(source));
		as.put(HibernateProxyConstants.UID, UUID.randomUUID().toString());
		as.put(HibernateProxyConstants.PKEY, key);
		as.put(HibernateProxyConstants.PROXYINITIALIZED, isInitialized);
		return as;
	}
}
