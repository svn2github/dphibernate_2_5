package org.dphibernate.serialization.writers

import java.util.List;

import org.dphibernate.model.Author;
import org.dphibernate.serialization.AbstractSerializerTest;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

class CollectionWriterTests extends AbstractSerializerTest
{

	/**
	 * Unit test for defect #21
	 * http://code.google.com/p/dphibernate/issues/detail?id=21
	 */
	@Test
	void serializesListOfObjectArray()
	{
		Author[] authors = [Author.withNameAndId("Josh Bloch", 1),Author.withNameAndId("Sondheim", 2)] as Author[]
		List<Author[]> source = [authors]
		List<Object> result = buildSerializer(source).serialize();
		assertThat(result[0], is(Object[])) // Should this be ASObject[]?
		Object[] serializedAuthors = result[0]
		assert serializedAuthors[0].isForEntity(authors[0])
		assert serializedAuthors[1].isForEntity(authors[1])
	}
}
