package org.dphibernate.serialization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.dphibernate.model.Author;
import org.dphibernate.model.BlogPost;
import org.dphibernate.model.Comment;
import org.dphibernate.model.Publisher;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

class CollectionSerializationTests extends AbstractSerializerTest
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
    	Publisher publisher = get(Publisher.class, 1);
    	ISerializer serializer = context.getSerializerFactory().getSerializer(publisher);
    	
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
    	Author author = (Author) get(Author.class, 1);
    	ISerializer serializer = context.getSerializerFactory().getSerializer(author);
    	
    	ASObject result = (ASObject) serializer.serialize();
    	List<ASObject> posts = (List<ASObject>) result.get("posts");
    	assertNotNull(posts);
    	assertEquals(4,posts.size());
    	
    	BlogPost realPost = (BlogPost) author.getPosts().get(0);
    	ASObject serializedPost = posts.get(0);
    	assertThat(BlogPost.class.getName(),equalTo(posts.get(0).getType()));
    	assertThat(BlogPost.class.getName(),equalTo(posts.get(1).getType()));
    	assertThat(Comment.class.getName(),equalTo(posts.get(2).getType()));
    	assertThat(Comment.class.getName(),equalTo(posts.get(3).getType()));
    }

}
