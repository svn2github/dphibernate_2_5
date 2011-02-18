package photoBrowser.tests.services;

import java.util.Collection;

import net.digitalprimates.persistence.hibernate.utils.HibernateUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import photoBrowser.services.MediaService;


public class TestInitializeDatabase
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
	public void testInitializeDatabase()
	{
		// open session
    	HibernateUtil.getCurrentSession();
    	HibernateUtil.beginTransaction();
		
			String dir = "C:/Users/mnimer/Pictures";
			new MediaService().initializeDatabase(dir);
			
		//HibernateUtil.getCurrentSession().flush();
        HibernateUtil.commitTransaction();
        
        
        HibernateUtil.beginTransaction();
        	Collection results = new MediaService().getAllMediaSets();
			System.out.println("Results: " +results.size());        
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
	}

}
