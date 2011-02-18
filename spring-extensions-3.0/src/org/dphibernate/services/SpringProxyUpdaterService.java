package org.dphibernate.services;


import org.dphibernate.persistence.state.IObjectChangeUpdater;
import org.dphibernate.persistence.state.IProxyResolver;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

public class SpringProxyUpdaterService extends ProxyUpdaterService 
{
	private final ApplicationContext applicationContext;
	public SpringProxyUpdaterService(SessionFactory sessionFactory,ApplicationContext applicationContext)
	{
		super(sessionFactory);
		this.applicationContext = applicationContext;
	}
	@Override
	IObjectChangeUpdater buildObjectChangeUpdater()
	{
		String[] beanNames = applicationContext.getBeanNamesForType(IObjectChangeUpdater.class);
		if (beanNames.length == 0)
		{
			return super.buildObjectChangeUpdater();
		}
		IObjectChangeUpdater bean = (IObjectChangeUpdater) applicationContext.getBean(beanNames[0]);
		return bean;
	}
	@Override
	IProxyResolver buildProxyResolver()
	{
		String[] beanNames = applicationContext.getBeanNamesForType(IProxyResolver.class);
		if (beanNames.length == 0)
		{
			return super.buildProxyResolver();
		}
		IProxyResolver bean = (IProxyResolver) applicationContext.getBean(beanNames[0]);
		return bean;
	}
}
