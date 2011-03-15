package org.dphibernate.serialization.writers;

import static org.junit.Assert.*;

import org.dphibernate.model.Book;
import org.dphibernate.model.TwoArraysOfBooks;
import org.dphibernate.serialization.AbstractSerializerTest;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

public class ArrayWriterTests extends AbstractSerializerTest
{

	@Test
	public void twoPropertiesOfSameArrayProduceSingleArray()
	{
		Book[] books = { new Book("Book1",1),new Book("Book2",2) };
		TwoArraysOfBooks source = new TwoArraysOfBooks(books, books);
		ASObject serialized = (ASObject) buildSerializer(source).serialize();
		// Note : source two arrays, but both are the same Book[] instance,
		// therefore the bookList1 & bookList2 in the serialized object
		// should be the same instance of ASObject[]
		assertSame(serialized.get("bookList1"), serialized.get("bookList2"));
	}
}
