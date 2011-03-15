package org.dphibernate.serialization.writers;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.dphibernate.model.Author;
import org.dphibernate.model.Book;
import org.dphibernate.serialization.AbstractSerializerTest;
import org.junit.Test;

public class AbstractPersistentCollectionWriterTests extends AbstractSerializerTest
{

	@Test
	public void givenSameCollectionPresentMultipleTimes_that_theCachedVersionIsReturned()
	{
		Author author = get(Author.class, 1);
		List<List<Book>> asList = Arrays.asList(author.getBooks(),author.getBooks());
		// Note : list has two elements, but both are the same hibernate collection of Book.
		// therefore the first & 2nd elements in the serialized list
		// should be the same instance of List<ASObject>
		fail("Not written");
	}
}
