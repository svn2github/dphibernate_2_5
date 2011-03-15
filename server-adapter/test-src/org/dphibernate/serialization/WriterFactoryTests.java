package org.dphibernate.serialization;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.dphibernate.model.Author;
import org.dphibernate.model.Publisher;
import org.dphibernate.serialization.writers.AbstractPersistentCollectionWriter;
import org.dphibernate.serialization.writers.ArrayWriter;
import org.dphibernate.serialization.writers.CollectionWriter;
import org.dphibernate.serialization.writers.EntityWriter;
import org.dphibernate.serialization.writers.MapWriter;
import org.dphibernate.serialization.writers.ObjectWriter;
import org.dphibernate.serialization.writers.ProxiedEntityWriter;
import org.dphibernate.serialization.writers.SimpleWriter;
import org.junit.Before;
import org.junit.Test;

public class WriterFactoryTests extends AbstractSerializerTest
{

	WriterFactory factory;
	@Before
	public void setup()
	{
		factory = context.getWriterFactory();
	}
	
	@Test
	public void testEntityDetection()
	{
		assertTrue(factory.isEntity(new Author()));
		assertTrue(factory.isEntity(get(Author.class, 1)));
		assertFalse(factory.isEntity(new Object()));
		assertFalse(factory.isEntity(new SimpleWriter()));
		assertFalse(factory.isEntity("Hello world"));
	}
	
	@Test
	public void testBasicWriters()
	{
		byte[] bytes = new byte[10];
		String[] strings = new String[] {"One"};
		
		assertThat(getWriterFor(bytes),is(SimpleWriter.class));
		assertThat(getWriterFor(new ArrayList()),is(CollectionWriter.class));
		assertThat(getWriterFor(new HashMap()),is(MapWriter.class));
		assertThat(getWriterFor(strings),is(ArrayWriter.class));
		assertThat(getWriterFor("Hello"),is(SimpleWriter.class));
		assertThat(getWriterFor(3),is(SimpleWriter.class));
		assertThat(getWriterFor(3l),is(SimpleWriter.class));
		assertThat(getWriterFor(3d),is(SimpleWriter.class));
		assertThat(getWriterFor(new Object()),is(ObjectWriter.class));
		assertThat(getWriterFor(new WriterFactoryTests()),is(ObjectWriter.class));
		assertThat(getWriterFor(Calendar.getInstance().getTime()),is(SimpleWriter.class));
	}
	private Object getWriterFor(Object object)
	{
		return factory.getWriter(object, new SerializerCache());
	}

	@Test
	public void testEntityWriters()
	{
		Author author;
		author = Author.withNameAndId("Joe", 1);
		assertThat(getWriterFor(author),is(ProxiedEntityWriter.class));
		
		author = get(Author.class, 1);
		assertThat(getWriterFor(author),is(ProxiedEntityWriter.class));

		Publisher publisher = Publisher.withNameAndId("Brooks", 1);
		assertThat(getWriterFor(publisher),is(EntityWriter.class));
		
		publisher = get(Publisher.class, 1);
		assertThat(getWriterFor(publisher),is(EntityWriter.class));
	}
	
	@Test
	public void testDatabaseWriters()
	{
		Author author = get(Author.class, 1);
		
		assertThat(getWriterFor(author),is(ProxiedEntityWriter.class));
		assertThat(getWriterFor(author.getBooks()),is(AbstractPersistentCollectionWriter.class));
	}
	
}
