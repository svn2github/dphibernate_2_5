package org.dphibernate.services;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

public class SpringDataAccessService extends DataAccessService
{

	public SpringDataAccessService(SessionFactory sessionFactory,ApplicationContext applicationContext)
	{
		super(sessionFactory,new SpringProxyUpdaterService(sessionFactory, applicationContext),
				new ProxyLoadService(sessionFactory),
				new ProxyBatchLoader(sessionFactory));
	}
	
}
