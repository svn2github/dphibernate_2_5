package org.dphibernate.serialization.writers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dphibernate.collections.PaginatedCollection;
import org.dphibernate.core.IEntity;
import org.dphibernate.serialization.DPHibernateASObject;
import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.serialization.IPropertySerializer;
import org.dphibernate.serialization.ISerializationWriter;
import org.dphibernate.serialization.ISerializer;
import org.dphibernate.utils.EntityUtil;
import org.dphibernate.utils.HibernateSessionManager;
import org.hibernate.proxy.HibernateProxy;

public class CollectionWriter extends AbstractSerialziationWriter implements ISerializationWriter<Collection<?>,Collection<Object>>
{

	boolean usePagination;
	int pageSize;
	
	public CollectionWriter(IPropertySerializer serializer)
	{
		super(serializer);
	}
	@Override
	public void populateStub(Collection<?> source, Collection<Object> stub)
	{
		boolean isPaginated = false;
		for (Object collectionMemeber : source)
		{
			Object translatedCollectionMember;
			if (usePagination && stub.size() > pageSize)
			{
				translatedCollectionMember = getPagedCollectionProxy(collectionMemeber);
				isPaginated = true;
			} else
			{
				translatedCollectionMember = serialize(collectionMemeber);
			}
			stub.add(translatedCollectionMember);
		}
		if (isPaginated)
		{
//			list = convertToPaginatedList(list);
		}
	}
	
	private List convertToPaginatedList(List list)
	{
		return new PaginatedCollection(list);
	}
	private Object getPagedCollectionProxy(Object collectionMemeber)
	{
		if (EntityUtil.isLazyProxy(collectionMemeber))
		{
			Object proxyKey = ((HibernateProxy)collectionMemeber).getHibernateLazyInitializer().getIdentifier();
			return DPHibernateASObject.uninitializedEntity(collectionMemeber, proxyKey);
		} else if (collectionMemeber instanceof IEntity)
		{
			Object proxyKey = ((IEntity) collectionMemeber).getEntityKey();
			return DPHibernateASObject.uninitializedEntity(collectionMemeber, proxyKey);
		} else
		{ // Default... we can't provide a proxy for this item, so translate it.
			return serialize(collectionMemeber);
		}

	}
	@Override
	public Collection<Object> createStubValue(Collection<?> source)
	{
		return new ArrayList<Object>();
	}


}
