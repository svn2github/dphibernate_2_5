package bugs.bug14;


import java.util.Collection;

import net.digitalprimates.persistence.hibernate.utils.HibernateUtil;
import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;
import net.digitalprimates.persistence.translators.SerializationFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bugs.bug5.domain.Menu;
import flex.messaging.io.amf.ASObject;

public class Bug14
{

	@Before
	public void setUp() throws Exception
	{
	}


	@After
	public void tearDown() throws Exception
	{
	}
	
	
	@Test
	public void returnInt()
	{
		// open session
    	HibernateUtil.getCurrentSession(true);

    	try
    	{
	    	int val = new ResultsGenerator().returnInt();
	    	
	    	String sessionFactoryClazz = "net.digitalprimates.persistence.hibernate.utils.HibernateUtil";
	        String method = "getCurrentSession";
	    	Object serializedValue = SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(sessionFactoryClazz, method, val);
	    	
	    	Assert.assertEquals(123, serializedValue);
	    	
    	}finally{
	        // open session
	    	HibernateUtil.closeSession();
    	}
	}

}
