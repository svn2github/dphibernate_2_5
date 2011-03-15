package org.dphibernate.serialization.writers;

import java.util.Collection;

import org.dphibernate.core.IEntity;
import org.dphibernate.proxy.EntityProxyFactory;
import org.dphibernate.proxy.IEntityProxy;
import org.dphibernate.serialization.DPHibernateASObject;
import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.PropertyHelper;
import org.dphibernate.serialization.SerializableProperty;
import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.utils.EntityUtil;

import flex.messaging.io.amf.ASObject;

/**
 * Writer for entity classes (eg., JPA / Hibernate entities)
 * that do not implement the IEntity interface.
 * 
 * Entities are proxied to implement IEntity during serialization
 * @author Marty Pitt
 *
 */
public class ProxiedEntityWriter extends AbstractObjectWriter<Object,ASObject>
{
	private final EntityProxyFactory proxyFactory;
	private IEntityProxy generatedProxy;

	public ProxiedEntityWriter(EntityProxyFactory entityProxyFactory, SerializerCache cache, ISerializerFactory serializerFactory, IPropertySerializer serializer)
	{
		super(cache, serializerFactory, serializer);
		this.proxyFactory = entityProxyFactory;
	}

	@Override
	public void populateStub(Object source,ASObject stub)
	{
		// By this stage, the proxy should've already been generated
		assert generatedProxy != null;
		super.populateStub(generatedProxy,stub);
	}
	@Override
	protected Collection<SerializableProperty> getPropertiesToSerialize(PropertyHelper helper)
	{
		// TODO Auto-generated method stub
		Collection<SerializableProperty> properties = super.getPropertiesToSerialize(helper);
		if (helper.getSource() instanceof IEntityProxy)
		{
			// Remove the properties that our proxy added.
			helper.removeProperty("target");
			helper.removeProperty("entityKey");
			helper.removeProperty("entityInitialized");
		}
		return helper.getProperties();
	}

	@Override
	protected ASObject getDefaultASObject(Object source)
	{
		generatedProxy = proxyFactory.buildProxy(source);
		return getDefaultASObject((IEntity) generatedProxy);
	}
	protected ASObject getDefaultASObject(IEntity entity)
	{
		boolean initialized = EntityUtil.isLazyProxy(entity);
		ASObject object = DPHibernateASObject.entity(entity, entity.getEntityKey(), initialized);
		if (entity instanceof IEntityProxy)
		{
			Class<?> targetClass = ((IEntityProxy) entity).getTarget().getClass();
			object.setType(targetClass.getName());
		}
		return object;
	}
}
