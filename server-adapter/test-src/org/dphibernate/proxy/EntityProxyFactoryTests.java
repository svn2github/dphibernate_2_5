package org.dphibernate.proxy;

import static org.junit.Assert.*;

import org.dphibernate.core.IEntity;
import org.dphibernate.model.Author;
import org.dphibernate.test.DbTestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EntityProxyFactoryTests extends DbTestCase
{
	EntityIdentityProvider identityProvider;
	@Before
	public void setup()
	{
		identityProvider = new HibernateIdentityProvider(getSessionFactory());
	}
	@Test
	public void generatesProxyForEntity()
	{
		EntityProxyFactory factory = new EntityProxyFactory(identityProvider);
		Author author = Author.withNameAndId("Josh Bloch", 1);
		IEntity proxy = factory.buildProxy(author);
		assertTrue(proxy instanceof IEntity);
		assertTrue(proxy instanceof Author);
		assertEquals(1,proxy.getEntityKey());
		assertEquals("Josh Bloch",((Author) proxy).getName());
	}
	
	@Test
	public void generatesProxyForEntityFromDatabase()
	{
		// Hibernate creates it's own proxies.  We don't wanna fuck with that.
		EntityProxyFactory factory = new EntityProxyFactory(identityProvider);
		Author author = get(Author.class, 1);
		IEntity proxy = factory.buildProxy(author);
		assertTrue(proxy instanceof IEntity);
		assertTrue(proxy instanceof Author);
		assertEquals(1,proxy.getEntityKey());
		assertEquals("Joshua Bloch",((Author) proxy).getName());
	}
}
