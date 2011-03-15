package org.dphibernate.context;

import static org.junit.Assert.*;

import org.dphibernate.model.Author;
import org.dphibernate.serialization.AbstractSerializerTest;
import org.dphibernate.serialization.ISerializer;
import org.junit.Test;

public class ContextBuilderTests extends AbstractSerializerTest
{
	@Test
	void simpleContextBuilds()
	{
		Context context = ContextBuilder.with(getSessionFactory())
			.withDefaultConfigurationThat()
			.hasPageSize(15)
			.andContextThat()
		.build();
		Author author = Author.withNameAndId("Bob", 1);
		
		ISerializer serializer = context.createSerializerFor(author).build();
		assertEquals(15,serializer.getConfiguration().getPageSize());
	}
}
