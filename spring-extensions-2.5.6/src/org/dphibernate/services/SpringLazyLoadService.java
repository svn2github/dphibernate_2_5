package org.dphibernate.services;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

public class SpringLazyLoadService extends LazyLoadService
{

	public SpringLazyLoadService(SessionFactory sessionFactory,ApplicationContext applicationContext)
	{
		super(sessionFactory,new ProxyLoadService(sessionFactory),
				new ProxyBatchLoader(sessionFactory));
	}
	
}
