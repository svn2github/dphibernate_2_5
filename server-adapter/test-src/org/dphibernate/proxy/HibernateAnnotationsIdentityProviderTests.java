package org.dphibernate.proxy;

import static org.junit.Assert.*;

import org.dphibernate.model.Author;
import org.dphibernate.test.DbTestCase;
import org.junit.Before;
import org.junit.Test;

public class HibernateAnnotationsIdentityProviderTests extends DbTestCase
{
	EntityIdentityProvider identityProvider;
	@Before
	public void setup()
	{
		identityProvider = new HibernateIdentityProvider(getSessionFactory());
	}
	 
	@Test
	public void testFindsIdOnEntity()
	{
		Author author = Author.withNameAndId("Bob", 1);
		Object id = identityProvider.getId(author);
		assertEquals(1,id);
	}
}
