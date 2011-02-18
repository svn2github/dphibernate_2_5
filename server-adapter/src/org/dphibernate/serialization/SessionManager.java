/**
	Copyright (c) 2008. Digital Primates IT Consulting Group
	http://www.digitalprimates.net
	All rights reserved.
	
	This library is free software; you can redistribute it and/or modify it under the 
	terms of the GNU Lesser General Public License as published by the Free Software 
	Foundation; either version 2.1 of the License.

	This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
	See the GNU Lesser General Public License for more details.

	
	@author: Mike Nimer
	@ignore
**/


package org.dphibernate.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.EventSource;

/**
 * This class is used to create and manage the hibernate session object. 
 * The parameters used in this class should be defined in the flex <Adapter> configuration. 
 * 
 * @author mike nimer
 *
 */
@SuppressWarnings("unchecked")
public class SessionManager 
{
    
    private SessionFactory sessionFactory;
    private Class sessionFactoryClazz;
    private String getCurrentSession;
    
	public SessionManager(String sessionFactoryClassName, String method)
	{

	    // Get the user-defined hibernate SessionFactory class and method. 
	    // we need this so we can re-attach the session for this new request, if needed.
	    Class sessionFactoryClazz = null;
	    try
		{
    		sessionFactoryClazz = Class.forName(sessionFactoryClassName);
		}catch(Exception ex){
	    	ex.printStackTrace();
			// throw error back to flex
			// todo: replace with custom exception
			RuntimeException re = new RuntimeException(ex.getMessage());
			re.setStackTrace(ex.getStackTrace());
			throw re;
	    }
            
        
		
		this.sessionFactoryClazz = sessionFactoryClazz;
		this.getCurrentSession = method;
	}
    
	

    /**
     * Using the user defined SessionFactory class and method we get the current
     * hibernate session or open a new one.
     * 
     * @return
     */
    public Session getCurrentSession()
    {
        Session session = null;
        try
        {
            Method method = sessionFactoryClazz.getMethod(getCurrentSession, (Class[])null);
            session = (Session)method.invoke(sessionFactoryClazz.newInstance(), (Object[])null);

            if (!session.isOpen())
            {
                throw new RuntimeException("Hibernate session is closed");
            }
            
            return session;
        }
        catch (IllegalAccessException ex){ex.printStackTrace();}
        catch (InstantiationException ex){ex.printStackTrace();}
        catch (NoSuchMethodException ex){ex.printStackTrace();}
        catch (InvocationTargetException ex){ ex.printStackTrace(); }
        
        throw new RuntimeException("Unable to obtain valid hibernate session");
    }
}
