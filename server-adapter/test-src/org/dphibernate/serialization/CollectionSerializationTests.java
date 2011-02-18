package org.dphibernate.serialization;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.dphibernate.model.Author;
import org.dphibernate.model.BugReport;
import org.dphibernate.model.Publisher;
import org.dphibernate.test.DbTestCase;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

public class CollectionSerializationTests extends DbTestCase
{
	@Override
	protected File getDataSet() throws Exception
	{
		File file = new File("test-src/DiscrimintaorColumnDataset.xml");
		assertTrue(file.exists());
		return file;
	}
	
    @Test
    public void serializesCollectionReturningProxies() {
    	Publisher publisher = (Publisher) getSessionFactory().getCurrentSession().get(Publisher.class, 1);
    	HibernateSerializer serializer = new HibernateSerializer(publisher);
    	serializer.setSessionFactory(getSessionFactory());
    	
    	ASObject result = (ASObject) serializer.serialize();
    	List<ASObject> authors = (List<ASObject>) result.get("authors");
    	assertNotNull(authors);
    	assertEquals(5,authors.size());
    	
    	ASObject author = authors.get(0);
    	String type = author.getType();
    	assertEquals(type,Author.class.getName());
    	Boolean proxyInit = (Boolean) author.get("proxyInitialized");
    	assertFalse(proxyInit);
    }

    @Test
    public void serializerReturnsCorrectTypeOfProxyWhenUsingDiscriminatorColumn()
    {
    	Author author = (Author) getSessionFactory().getCurrentSession().get(Author.class, 1);
    	HibernateSerializer serializer = new HibernateSerializer(author);
    	serializer.setSessionFactory(getSessionFactory());
    	
    	ASObject result = (ASObject) serializer.serialize();
    	List<ASObject> posts = (List<ASObject>) result.get("posts");
    	assertNotNull(posts);
    	assertEquals(4,posts.size());
    	
    	BugReport realPost = (BugReport) author.getPosts().get(0);
    	ASObject serializedPost = posts.get(0);
    	assertEquals(realPost.getClass().getName(),serializedPost.getType());
    }

}
