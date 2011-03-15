package org.dphibernate.serialization.writers

import java.util.ArrayList;

import org.dphibernate.core.HibernateProxyConstants;
import org.dphibernate.model.Author;
import org.dphibernate.serialization.AbstractSerializerTest;
import org.dphibernate.serialization.CollectionMatcher;
import org.dphibernate.serialization.EntityMatcher;
import org.dphibernate.serialization.HasPropertiesMatcher;
import org.dphibernate.serialization.HibernateSerializer;
import org.junit.Before;
import org.junit.Test;

import com.sun.xml.internal.stream.Entity;
import static org.junit.Assert.*;

import flex.messaging.io.amf.ASObject;

class EntityWriterTests extends AbstractSerializerTest
{
	@Test
	public void testWritingEntity()
	{
		def author = get(Author.class, 1);
		def serializer = buildSerializer(author);
		ASObject result = serializer.serialize();
		assert result.isForEntity(author)
		assert result.hasProperties(["name","age"]).from(author)
		assert result.hasLazyCollection('books').matching(author.books);
		assert result.hasLazyEntity("publisher").matching(author.publisher);
	}
	@Test
	public void whenTwoInstancesOfSameEntityArePresent_that_cachedVersionIsUsed()
	{
		def author = get(Author.class, 1);
		def serializer = buildSerializer([author,author]);
		List result = serializer.serialize();
		// The list contained two entires of the same instance of entity.
		// therefore the serialized list should contain two instances of the same ASObject
		assertSame(result[0], result[1])
	}
	
	
}
