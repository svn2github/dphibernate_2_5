package photoBrowser.tests.services;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import net.digitalprimates.persistence.hibernate.utils.HibernateUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import photoBrowser.services.MediaService;

public class TestGetAll
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
	public void testGetAllMediaSets()
	{
		// open session
    	HibernateUtil.getCurrentSession();
    	HibernateUtil.beginTransaction();
			
			Collection results = new MediaService().getAllMediaSets();
			System.out.println("Results: " +results.size());
			
		HibernateUtil.getCurrentSession().flush();
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
	
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
	}
	

}
