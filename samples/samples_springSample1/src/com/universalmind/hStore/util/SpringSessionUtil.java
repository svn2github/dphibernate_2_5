package com.universalmind.hStore.util;

import javax.servlet.ServletContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import flex.messaging.FlexContext;


//nimer: rename from HibernateUtil
public class SpringSessionUtil 
{
	//nimer: make static
	public static Session getCurrentSession() 
	{		
		ServletContext ctx = FlexContext.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);
		SessionFactory sessionFactory = (SessionFactory)springContext.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, false);
		return session;
    }
	
}
