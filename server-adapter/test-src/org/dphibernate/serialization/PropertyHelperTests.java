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

public class PropertyHelperTests
{

	@Test
	public void allPropertiesAreReturned()
	{
		Author author = TestDataProvider.getAuthor();
		Map<String, Object> properties = PropertyHelper.getProperties(author);
		assertContainsAllProperties(asList("proxyKey","proxyInitialized","id","name","age","books","publisher"),properties);
	}
	@Test
	public void neverSerializeIsNotReturned()
	{
		User user = new User("myuser", "mypassword");
		Map<String, Object> properties = PropertyHelper.getProperties(user);
		assertFalse(properties.containsKey("password"));
	}
	@Test
	public void valuesArePresent()
	{
		Author author = TestDataProvider.getAuthor();
		Map<String, Object> properties = PropertyHelper.getProperties(author);
		assertEquals(properties.get("proxyKey"), author.getProxyKey());
		assertEquals(properties.get("proxyInitialized"), author.getProxyInitialized());
		assertEquals(properties.get("age"), author.getAge());
		assertEquals(properties.get("books"), author.getBooks());
		assertEquals(properties.get("id"), author.getId());
		assertEquals(properties.get("name"), author.getName());
		assertEquals(properties.get("publisher"), author.getPublisher());
		
	}
	@Test
	public void nullsDontCauseExceptions()
	{
		Author author = TestDataProvider.getAuthor();
		author.setPublisher(null);
		Map<String, Object> properties = PropertyHelper.getProperties(author);
		assertTrue(properties.containsKey("publisher"));
		assertNull(properties.get("publisher"));
	}
	private void assertContainsAllProperties(List<String> propertyNames,Map<String, Object> properties)
	{
		for (String propertyName:propertyNames)
		{
			assertTrue(properties.containsKey(propertyName));
		}
	}
}
