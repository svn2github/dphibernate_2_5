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

package org.dphibernate.utils;

import java.sql.SQLException;


import org.dphibernate.serialization.ISerializerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;

/**
 * A utility class to help you manage your Hibernate sessions
 * 
 * @author mike nimer
 */
		
public class HibernateSessionManager
{

    private static SessionFactory sessionFactory;
	
    public static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

    public static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

    public static SessionFactory getSessionFactory() throws HibernateException
    {
    	return sessionFactory;
    }
    public static void setSessionFactory(SessionFactory sessionFactory)
    {
    	HibernateSessionManager.sessionFactory = sessionFactory;
    }
}
