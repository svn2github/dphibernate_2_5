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

/**
 * A utility class to help you manage your Hibernate sessions
 * 
 * @author mike nimer
 */
		
public class HibernateUtil
{

//    private static SessionFactory sessionFactory;
	
	private static ISerializerFactory serializerFactory;
    public static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

    public static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

    public static SessionFactory getSessionFactory() throws HibernateException
    {
    	return serializerFactory.getSessionFactory();
    }
	public static Session getCurrentSession() throws HibernateException
    {
    	return getCurrentSession(false);
    }
    
    
    public static Session getCurrentSession(Boolean forceNewConnection) throws HibernateException
    {
    	Session s = null;
    	if( !forceNewConnection )
    	{
    		s = threadSession.get();
    	}
    	
        // Open a new Session, if this Thread has none yet
        if (s == null)
        {
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
        return s;
    }


    public static void closeSession() throws HibernateException
    {
        Session s = threadSession.get();
        threadSession.set(null);
        try
        {
        	if (s != null && !s.connection().isClosed()) s.close();
        }
        catch( SQLException ex){}
    }


    public static Transaction getTransaction()
    {
    	return threadTransaction.get();
    }
    
    
    public static void beginTransaction()
    {
        Transaction tx = threadTransaction.get();
        if (tx == null)
        {
            tx = getCurrentSession().beginTransaction();
            threadTransaction.set(tx);
        }
    }
    public static void beginTransaction(Session session)
    {
    	Transaction tx = threadTransaction.get();
        if (tx == null)
        {
            tx = session.beginTransaction();
            threadTransaction.set(tx);
        }
    }


    public static void commitTransaction()
    {
        Transaction tx = threadTransaction.get();
        try
        {
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) tx.commit();
            threadTransaction.set(null);
        }
        catch (HibernateException ex)
        {
            rollbackTransaction();
            throw ex;
        }
    }


    public static void rollbackTransaction()
    {
        Transaction tx = threadTransaction.get();
        threadTransaction.set(null);
        try
        {
            if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
            {
                tx.rollback();
            }
        }
        finally
        {
            closeSession();
        }

    }

	public static void setSerializerFactory(ISerializerFactory serializerFactory)
	{
		HibernateUtil.serializerFactory = serializerFactory;
	}


	public static ISerializerFactory getSerializerFactory()
	{
		return serializerFactory;
	}
}
