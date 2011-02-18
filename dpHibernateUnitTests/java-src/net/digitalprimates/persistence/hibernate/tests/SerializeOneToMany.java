package net.digitalprimates.persistence.hibernate.tests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.collection.PersistentCollection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;

import net.digitalprimates.persistence.hibernate.proxy.HibernateProxy;
import net.digitalprimates.persistence.hibernate.tests.oneToMany.O2MAddress;
import net.digitalprimates.persistence.hibernate.tests.oneToMany.O2MPerson;
import net.digitalprimates.persistence.hibernate.utils.HibernateUtil;
import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;
import net.digitalprimates.persistence.translators.SerializationFactory;

@SuppressWarnings("unchecked")
public class SerializeOneToMany
{
    O2MPerson u1;
    
    
    @Before
    public void setUp() throws Exception
    {
    	// open session
    	HibernateUtil.getCurrentSession();
    	HibernateUtil.beginTransaction();
    	
    	
        u1 = new O2MPerson();
        u1.firstName = "test 1";
        u1.lastName = "user 1";
        u1.addresses = new ArrayList();
        
        O2MAddress address1 = new O2MAddress();
        address1.address1 = "123 main st";
        address1.city = "Boston";
        address1.state = "MA";
        
        O2MAddress address2 = new O2MAddress();
        address2.address1 = "123 main st";
        address2.city = "Boston";
        address2.state = "MA";
        
        u1.addresses.add(address1);
        u1.addresses.add(address2);
        
        
        u1 = (O2MPerson)new HibernateService().merge(u1);
        
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }
    

    @After
    public void tearDown() throws Exception
    {
    	// open session
    	HibernateUtil.getCurrentSession(true);
    	
        new HibernateService().delete(u1, true);

        HibernateUtil.closeSession();
    }
    

    @Test
    public void lazyOneToMany()
    {
    	// open session
    	HibernateUtil.getCurrentSession(true);

    	try
    	{
	    	String sql = "select * from o2mPerson";
	    	List results = new HibernateService().executeSql(sql);
	    	
	    	O2MPerson user = (O2MPerson)new HibernateService().load(O2MPerson.class, u1.id);
	    	
	    	Assert.assertNotNull(user); // setUp failed
	        Assert.assertTrue(user instanceof O2MPerson);
	        
	        // make sure everything is still lazy and has not been loaded.
	        //Assert.assertTrue( "Instance of wrong datatype", user.connectInfo instanceof HibernateProxy );
	        //Assert.assertTrue( "Proxy already initialized", !((HibernateProxy)user.connectInfo).proxyInitialized );
	        Assert.assertTrue( "Instance of wrong datatype", user.addresses instanceof PersistentCollection );
	        Assert.assertTrue( "Collection already initialized", !((PersistentCollection)user.addresses).wasInitialized() );
	        
	        String sessionFactoryClazz = "net.digitalprimates.persistence.hibernate.utils.HibernateUtil";
	        String method = "getCurrentSession";
	        
	        ASObject sUser = (ASObject) SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(sessionFactoryClazz, method, user);
	        
	        Assert.assertTrue(((Collection) sUser.get("addresses")).size() > 0);
	        Assert.assertTrue(((Collection) sUser.get("addresses")).toArray()[0] instanceof ASObject);
	        Assert.assertTrue("net.digitalprimates.persistence.hibernate.tests.oneToMany.O2MAddress".equals( ((ASObject) ((Collection) sUser.get("addresses")).toArray()[0]).getType()) );
	        
    	}finally{
	        // open session
	    	HibernateUtil.closeSession();
    	}
    }
    
    
    @Test
    public void notLazyOneToMany()
    {
    	// open session
    	HibernateUtil.getCurrentSession(true);
    	
    	try
    	{
    		String sql = "select * from o2mPerson";
    		List results = new HibernateService().executeSql(sql);
    		
    		O2MPerson user = (O2MPerson)new HibernateService().load(O2MPerson.class, u1.id);

    		// touch the properties to everything has been populated before we serialize the object
    		Iterator itr = user.getAddresses().iterator();
    		while( itr.hasNext() )
    		{
    			itr.next();
    		}
    		
    		Assert.assertNotNull(user); // setUp failed
    		Assert.assertTrue(user instanceof O2MPerson);
    		
    		// make sure everything is still lazy and has not been loaded.
    		//Assert.assertTrue( "Instance of wrong datatype", user.connectInfo instanceof HibernateProxy );
    		//Assert.assertTrue( "Proxy already initialized", !((HibernateProxy)user.connectInfo).proxyInitialized );
    		Assert.assertTrue( "Instance of wrong datatype", user.addresses instanceof PersistentCollection );
    		Assert.assertTrue( "Collection not initialized", ((PersistentCollection)user.addresses).wasInitialized() );
    		
    		String sessionFactoryClazz = "net.digitalprimates.persistence.hibernate.utils.HibernateUtil";
    		String method = "getCurrentSession";
    		
    		ASObject sUser = (ASObject) SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(sessionFactoryClazz, method, user);
    		
    		Assert.assertTrue(((Collection) sUser.get("addresses")).size() > 0);
    		Assert.assertTrue(((Collection) sUser.get("addresses")).toArray()[0] instanceof ASObject);
    		Assert.assertTrue("net.digitalprimates.persistence.hibernate.tests.oneToMany.O2MAddress".equals( ((ASObject) ((Collection) sUser.get("addresses")).toArray()[0]).getType()) );
    		
    	}finally{
    		// open session
    		HibernateUtil.closeSession();
    	}
    }
    
}
