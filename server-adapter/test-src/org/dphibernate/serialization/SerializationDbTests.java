package org.dphibernate.serialization;

import org.dphibernate.model.Author;
import org.dphibernate.test.DbTestCase;
import org.junit.Test;

public class SerializationDbTests extends DbTestCase
{

	@Test
	public void test()
	{
		Author author= get(Author.class, 1);
	}
	
}
