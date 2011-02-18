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


package org.dphibernate.filters;

import javax.annotation.Resource;

import org.dphibernate.filters.AbstractHibernateSessionServletFilter;
import org.hibernate.SessionFactory;


/**
 * Servlet Filter to open a close the Hibernate session for each request.
 * 
 * based on the servlet filter code: http://www.hibernate.org/43.html.
 * 
 * This instance uses an injected SessionFactory, and is suitable for use with a DI container such as Spring
 * 
 * @author Mike Nimer, Marty Pitt
 */
public class SpringHibernateSessionServletFilter extends AbstractHibernateSessionServletFilter
{
	@Resource
	private SessionFactory sessionFactory;


	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Override
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
}
