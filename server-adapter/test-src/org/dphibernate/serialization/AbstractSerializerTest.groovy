package org.dphibernate.serialization

import org.dphibernate.context.Context;
import org.dphibernate.context.ContextBuilder;
import org.dphibernate.core.HibernateProxyConstants;
import org.dphibernate.proxy.EntityProxyFactory;
import org.dphibernate.proxy.HibernateIdentityProvider;
import org.dphibernate.test.DbTestCase;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;

import flex.messaging.io.amf.ASObject;

abstract class AbstractSerializerTest extends DbTestCase
{
	protected EntityProxyFactory proxyFactory;
	def cache = new SerializerCache()
	protected Context context;
	
	protected ISerializer buildSerializer(Object source)
	{
		return context.createSerializerFor(source).build()
	}

	@Before
	public void setup_AbstractSerializerTest()
	{	
		initializeHelpers();
		context = ContextBuilder.with(sessionFactory).build()
		proxyFactory = context.proxyFactory;
		
	}
	public void initializeHelpers()
	{
		ASObject.metaClass.isForEntity = { entity ->
			assert delegate.getType() == entity.class.name
			assert delegate[HibernateProxyConstants.PKEY] == entity.id
			return true
		}
		ASObject.metaClass.hasProperties = { String properties ->
			return new HasPropertiesMatcher(delegate,properties)
		}
		ASObject.metaClass.hasProperties << { properties ->
		return new HasPropertiesMatcher(delegate,properties)
		}
		
		ASObject.metaClass.hasLazyCollection = { propertyName ->
			return new CollectionMatcher(delegate, propertyName, true)
		}
		ASObject.metaClass.hasLazyEntity = { propertyName ->
			return new EntityMatcher(delegate,propertyName,true)
		}
		
		ArrayList.metaClass.hasLazyEntity = { entity ->
			delegate.find { element ->
				elementMatchesEntity(element, entity, false)
			}
		}
		ArrayList.metaClass.isCollectionOfProxies = {
			delegate.each { assert it.isProxy }
			return true
		}
		ASObject.metaClass.methodMissing = { String name, args ->
			println "$name() called with $args"
		}
		/**
		 * ASserts that this ASObject is not a lazy loaded proxy,
		 * ie - that all of it's properties' values have been included
		 */
		ASObject.metaClass.isNotLazyProxy = { ->
			assert delegate[HibernateProxyConstants.PROXYINITIALIZED] == true
			return true;
		}
		ASObject.metaClass.isProxy = { ->
			assert delegate[HibernateProxyConstants.PROXYINITIALIZED] == false
			return true;
		}
	}
	
	def elementMatchesEntity = {element,entity,proxyInitialized ->
		element.getType() == entity.class.name &&
			element[HibernateProxyConstants.PKEY] == entity.id &&
			element[HibernateProxyConstants.PROXYINITIALIZED] == proxyInitialized
	}

	
}
