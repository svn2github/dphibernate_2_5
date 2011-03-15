package org.dphibernate.serialization;

import static org.junit.Assert.*;

import java.util.List;


import org.dphibernate.model.Author;
import org.dphibernate.serialization.SerializerCache;
import org.dphibernate.serialization.HibernateSerializer;
import org.junit.Before;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

/**
 * The 'normal' situation is that we'd be serializing
 * an object from Hibernate, so generally properties on the object
 * which are entities, and collections, would be hibernate proxies, waiting
 * to be lazy-loaded from the db.
 * 
 * In some cases however, we have a fully populated object graph.  We want to
 * ensure that in these cases, we're serializing with the same efficiency as
 * if there were hibernate proxies present.
 */
public class HibernateSerializerTestsOnObjectsThatDontHaveHibernateProxies extends AbstractSerializerTest
{

	@Test
	public void whenSerializingCollectionsAreProxied()
	{
		ISerializer serializer = buildSerializer(TestDataProvider.getAuthor());
		ASObject result = (ASObject) serializer.serialize();
		assert result.isNotLazyProxy
		List<ASObject> books = result["books"]
		assertNotNull books;
		assert books.isCollectionOfProxies 
	}
	@Test
	public void whenSerializingChildPropertiesAreProxied()
	{
		ISerializer serializer = buildSerializer(TestDataProvider.getAuthor());
		ASObject result = (ASObject) serializer.serialize();
		assertASObjectIsNotProxy(result);
		ASObject publisher = getProperty(result, "publisher");
		assertASObjectIsProxy(publisher);
	}
	private List<ASObject> getCollection(ASObject source, String property)
	{
		return (List<ASObject>) source.get(property);
	}
	private ASObject getProperty(ASObject source,String property)
	{
		return (ASObject) source.get(property);
	}
}
