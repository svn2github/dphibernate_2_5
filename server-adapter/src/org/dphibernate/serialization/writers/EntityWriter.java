package org.dphibernate.serialization.writers;

import org.dphibernate.core.IEntity;
import org.dphibernate.serialization.DPHibernateASObject;
import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.utils.EntityUtil;

import flex.messaging.io.amf.ASObject;

public class EntityWriter extends AbstractObjectWriter<IEntity,ASObject>
{
	public EntityWriter(SerializerCache cache, ISerializerFactory serializerFactory, IPropertySerializer serializer)
	{
		super(cache, serializerFactory, serializer);
	}

	protected ASObject getDefaultASObject(IEntity source)
	{
		boolean initialized = EntityUtil.isLazyProxy(source);
		return DPHibernateASObject.entity(source, source.getEntityKey(), initialized);
	}
}
