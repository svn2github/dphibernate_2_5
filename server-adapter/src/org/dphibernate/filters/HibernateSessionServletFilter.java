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

import org.dphibernate.utils.HibernateUtil;
import org.hibernate.SessionFactory;


/**
 * Servlet Filter to open a close the Hibernate session for each request.
 * 
 * based on the servlet filter code: http://www.hibernate.org/43.html
 * 
 * @author mike nimer, Marty Pitt
 */
public class HibernateSessionServletFilter extends AbstractHibernateSessionServletFilter
{

	@Override
	public SessionFactory getSessionFactory()
	{
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		return sessionFactory;
	}

}
