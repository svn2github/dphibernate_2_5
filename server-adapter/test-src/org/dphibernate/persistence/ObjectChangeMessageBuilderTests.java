package org.dphibernate.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.dphibernate.model.Book;
import org.dphibernate.persistence.state.ObjectChangeMessage;
import org.dphibernate.persistence.state.ObjectChangeMessageBuilder;
import org.dphibernate.persistence.state.PropertyChangeMessage;
import org.junit.Test;

public class ObjectChangeMessageBuilderTests
{

	@Test
	public void buildsForClassCorrectly()
	{
		ObjectChangeMessage changeMessage = new ObjectChangeMessageBuilder(Book.class).build();
		assertEquals(Book.class.getCanonicalName(),changeMessage.getOwner().getRemoteClassName());
	}
	
	@Test
	public void buildsNew()
	{
		ObjectChangeMessage changeMessage = new ObjectChangeMessageBuilder(Book.class).asNew().build();
		assertTrue(changeMessage.getIsNew());
	}
	
	@Test
	public void setsChangedProperties()
	{
		Book book;
		ObjectChangeMessage changeMessage = new ObjectChangeMessageBuilder(Book.class).sets("author").to("newValue").build();
		
		assertTrue(changeMessage.containsChangeToProperty("author"));
		PropertyChangeMessage propertyChange = changeMessage.getPropertyChange("author");
		assertEquals("newValue", propertyChange.getNewValue());
		assertNull(propertyChange.getOldValue());
	}
	
}
