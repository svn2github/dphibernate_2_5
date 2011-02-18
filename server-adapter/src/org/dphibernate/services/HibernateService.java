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

package org.dphibernate.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dphibernate.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

/**
 * A simple wrapper class for the basic Hibernate calls. This class is meant to be extended by 
 * your Flex services and not called directly.
 * 
 *  Deprecated.  Use DataAccessService  or SpringDataAccessService instead.
 * @author mike nimer
 */
@SuppressWarnings("unchecked")
@Deprecated
public class HibernateService
{
    private static final Log log = LogFactory.getLog(HibernateService.class);
    public List list(Class cls)
    {
        return list(cls, null);
    }


    public List list(Class cls, DetachedCriteria criteria)
    {
    	return list(cls, null, criteria);
    }
    
    
    public List list(Class cls, Integer maxRows, DetachedCriteria criteria)
    {
        Session session = null;
      List result = new ArrayList();

        try
        {
            session = HibernateUtil.getCurrentSession();

            long tStart = new Date().getTime();             
            if( criteria != null )
            {
                result = criteria.getExecutableCriteria(session).list();                
            }
            else
            {
                Criteria crt = session.createCriteria(cls);
                crt = crt.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                result = crt.list();
            }
            
            long tEnd = new Date().getTime();
            log.debug("{list()}" +(tEnd-tStart) +"ms  class=" +cls.getName() +" size=" +result.size());
 

        }
        catch (HibernateException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (RuntimeException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (Throwable ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
        }

        return result;
    }


    public Object loadBean(Class cls, Serializable id)
    {
        Session session = null;
         Object result;

        try
        {
            session = HibernateUtil.getCurrentSession();
            
            long tStart = new Date().getTime();
                result = session.get(cls, id);
            long tEnd = new Date().getTime();
            log.debug("{load()}" +(tEnd-tStart) +"ms  class=" +cls.getName() );
            
        }
        catch (HibernateException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (RuntimeException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }

        return result;
    }


    public Object save(Object object)
    {
        Session session = null;
        Object result = null;

        try
        {
            session = HibernateUtil.getCurrentSession();
            
            long tStart = new Date().getTime();
                result = session.save(object);      
            long tEnd = new Date().getTime();
            log.debug("{save()}" +(tEnd-tStart) +"ms  class=" +object.getClass().getName() );
            
        }
        catch (HibernateException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (RuntimeException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        
        
        return result;
    }
    
    
    public Object merge(Object object)
    {
        Session session = null;
        Object result = null;
        
        try
        {
            session = HibernateUtil.getCurrentSession();
            
            long tStart = new Date().getTime();
            result = session.merge(object);                
            long tEnd = new Date().getTime();
            log.debug("{save()}" +(tEnd-tStart) +"ms  class=" +object.getClass().getName() );
            
        }
        catch (HibernateException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (RuntimeException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }


        return result;
    }


    public void delete(Object obj, boolean hardDelete)
    {
        Session session = null;
 
        try
        {
            session = HibernateUtil.getCurrentSession();
 
            long tStart = new Date().getTime();

                if (hardDelete)
                {
                    session.delete(obj);
                }
                else
                {
                    // obj.setIsDeleted(0);
                    session.merge(obj);
                }
                            
            long tEnd = new Date().getTime();
            log.debug("{delete()}" +(tEnd-tStart) +"ms  class=" +obj.getClass().getName() );
                    
            
        }
        catch (HibernateException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (RuntimeException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
    }
    
    
    public List executeSql(String sql)
    {
    	return executeSql(sql, null);
    }
    
    
    public List executeSql(String sql, Class entity )
    {
        Session session = null;
        List result = new ArrayList();

        try
        {
            session = HibernateUtil.getCurrentSession();

            long tStart = new Date().getTime(); 
            
                SQLQuery query = session.createSQLQuery(sql);
                if( entity != null )
                {
                	query.addEntity(entity);
                }
                
                result = query.list();
            
            long tEnd = new Date().getTime();
            log.debug("{list()}" +(tEnd-tStart) +"ms  size=" +result.size() +"\n sql=" +sql);
 
        }
        catch (HibernateException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (RuntimeException ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
            throw ex;
        }
        catch (Throwable ex)
        {
            HibernateUtil.rollbackTransaction();
            ex.printStackTrace();
        }

        return result;
    }
    
    
    public List executeHql(String hql)
    {
    	Session session = null;
    	List result = new ArrayList();
    	
    	try
    	{
    		session = HibernateUtil.getCurrentSession();
    		
    		long tStart = new Date().getTime(); 
    		
    		Query query = session.createQuery(hql);
    		result = query.list();
    		
    		long tEnd = new Date().getTime();
    		log.debug("{list()}" +(tEnd-tStart) +"ms  size=" +result.size());
    		
    	}
    	catch (HibernateException ex)
    	{
    		HibernateUtil.rollbackTransaction();
    		ex.printStackTrace();
    		throw ex;
    	}
    	catch (RuntimeException ex)
    	{
    		HibernateUtil.rollbackTransaction();
    		ex.printStackTrace();
    		throw ex;
    	}
    	catch (Throwable ex)
    	{
    		HibernateUtil.rollbackTransaction();
    		ex.printStackTrace();
    	}

    	return result;
    }

}
