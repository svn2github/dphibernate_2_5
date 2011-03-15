package org.dphibernate.serialization;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;


import org.dphibernate.model.Author;
import org.dphibernate.model.User;
import org.junit.Test;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class PropertyHelperTests extends AbstractSerializerTest
{

	@Test
	public void allPropertiesAreReturned()
	{
		Author author = TestDataProvider.getAuthor();
		PropertyHelper propertyHelper = new PropertyHelper(author);
		
		["id","name","age","books","publisher"].each { 
			assert propertyHelper.containsPropertyWithName(it)
			}
	}
	@Test
	public void neverSerializeIsNotReturned()
	{
		User user = new User("myuser", "mypassword");
		PropertyHelper propertyHelper = new PropertyHelper(user)
		assertFalse propertyHelper.containsPropertyWithName("password")
	}

	@Test
	public void nullsDontCauseExceptions()
	{
		Author author = TestDataProvider.getAuthor();
		author.setPublisher(null);
		PropertyHelper propertyHelper = new PropertyHelper(author);
		assertTrue propertyHelper.containsPropertyWithName("publisher");
		assertNull(propertyHelper.getPropertyValue("publisher"))
	}
	
}
