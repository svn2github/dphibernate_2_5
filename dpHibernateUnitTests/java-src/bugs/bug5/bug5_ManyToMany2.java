package bugs.bug5;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.digitalprimates.persistence.hibernate.HibernateAdapter;
import net.digitalprimates.persistence.hibernate.tests.oneToMany.O2MPerson;
import net.digitalprimates.persistence.hibernate.utils.HibernateUtil;
import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;
import net.digitalprimates.persistence.translators.SerializationFactory;

import org.hibernate.collection.PersistentCollection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import flex.messaging.io.amf.ASObject;
import flex.messaging.messages.RemotingMessage;
import flex.messaging.services.messaging.RemoteMessageClient;

import bugs.bug5.domain.Menu;
import bugs.bug5.domain.Permission;
import bugs.bug5.domain.Role;

public class bug5_ManyToMany2
{
	private Menu menu1;
	private Long menu1id;
	
	@Before
	public void setUp() throws Exception
	{
		menu1 = new Menu();
		menu1.setMenuID( new Date().getTime() );
		menu1.setName("test menu");
		Set permissionSet = new HashSet(0);

			Permission p1 = new Permission();
			p1.setPermissionID( new Date().getTime()+1 );
			p1.setEnabled(true);
			p1.setName("permission 1");
			p1.setRemark("Permission remark");
			p1.setType("admin");
			
				Role role1 = new Role();
				role1.setRoleID( UUID.randomUUID().toString() );
				role1.setName("role 1");
				role1.setRemark("Role remark");
				
				Role role2 = new Role();
				role2.setRoleID( UUID.randomUUID().toString() );
				role2.setName("role 1");
				role2.setRemark("Role remark");
				
				HashSet roleSet = new HashSet();
				roleSet.add(role1);
				roleSet.add(role2);
				
				p1.setRoles(roleSet);
			
		permissionSet.add( p1 );
		menu1.setPermissions( permissionSet );
		
		
		// open session
    	HibernateUtil.getCurrentSession();
    	HibernateUtil.beginTransaction();
    	
    	try
    	{
			menu1id = (Long)new HibernateService().save(menu1);
    	}catch( Exception ex){
    		ex.printStackTrace();
    	}
    	finally
    	{
	        HibernateUtil.commitTransaction();
	        HibernateUtil.closeSession();
    	}
	}


	@After
	public void tearDown() throws Exception
	{
    	// open session
    	HibernateUtil.getCurrentSession(true);
    	HibernateUtil.beginTransaction();
    	
        new HibernateService().delete(menu1, true);

        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();		
	}

	
	@Test
	public void testMenuPermissionRoles()
	{
		// open session
    	HibernateUtil.getCurrentSession(true);

    	try
    	{
	    	Menu menu = (Menu)new HibernateService().load(Menu.class, menu1id);
	    	
	    	Assert.assertNotNull(menu);

	    	// touch permissions so it will be loaded and we can check roles lazy property.
	    	((Permission)menu.getPermissions().toArray()[0]).getName();
	    	
	    	String sessionFactoryClazz = "net.digitalprimates.persistence.hibernate.utils.HibernateUtil";
	        String method = "getCurrentSession";
	    	ASObject fMenu = (ASObject) SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(sessionFactoryClazz, method, menu);
	    	
	    	Assert.assertNotNull(fMenu);
	    	Assert.assertTrue(((Collection) fMenu.get("permissions")).size() > 0);
	        Assert.assertTrue(((Collection) fMenu.get("permissions")).toArray()[0] instanceof ASObject);
	        Assert.assertFalse( ((Boolean) ((ASObject) ((Collection) ((ASObject) ((Collection) fMenu.get("permissions")).toArray()[0]).get("roles")).toArray()[0]).get("proxyInitialized")) );
	        
    	}finally{
	        // open session
	    	HibernateUtil.closeSession();
    	}
	}
	
	
	@Test
	public void testSaveMenuPermissionRoles()
	{
		// open session
		HibernateUtil.getCurrentSession(true);
		
		try
		{
			Menu menu = (Menu)new HibernateService().load(Menu.class, menu1id);
			
			Assert.assertNotNull(menu);
			
			// touch permissions so it will be loaded and we can check roles lazy property.
			((Permission)menu.getPermissions().toArray()[0]).getName();
			
			String sessionFactoryClazz = "net.digitalprimates.persistence.hibernate.utils.HibernateUtil";
			String method = "getCurrentSession";
			ASObject fMenu = (ASObject) SerializationFactory.getSerializer(SerializationFactory.HIBERNATESERIALIZER).translate(sessionFactoryClazz, method, menu);

			
		}finally{
			// open session
			HibernateUtil.closeSession();
		}
	}
}
