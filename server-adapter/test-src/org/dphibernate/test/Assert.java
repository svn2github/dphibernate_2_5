package org.dphibernate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import flex.messaging.io.amf.ASObject;

public class Assert
{
	public static void assertIsCollectionOfProxies(List<ASObject> objects)
	{
		for (ASObject asObject : objects)
		{
			assertASObjectIsProxy(asObject);
		}
	}

	public static void assertASObjectIsNotProxy(ASObject result)
	{
		Object proxyInit = result.get("proxyInitialized");
		assertNotNull(proxyInit);
		assertTrue((Boolean)proxyInit);
	}
	public static void assertASObjectIsProxy(ASObject result)
	{
		Object proxyInit = result.get("proxyInitialized");
		// In some cases, the proxyInitialized value isn't set on the ASOjbect, as it arrives as false by default on the client
		if (proxyInit == null) return;
		assertFalse((Boolean)proxyInit);
	}
	public static void assertIsObjectFor(ASObject object,Class<?> objectType)
	{
		assertEquals(objectType.getName(),object.getType());
	}
}
