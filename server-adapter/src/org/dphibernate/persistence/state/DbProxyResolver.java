package org.dphibernate.persistence.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.dphibernate.core.IHibernateProxy;
import org.hibernate.SessionFactory;

public class DbProxyResolver implements IProxyResolver {

	private Map<Object, IHibernateProxy> inProcessProxies = new HashMap<Object, IHibernateProxy>();
	private SessionFactory sessionFactory;
	public DbProxyResolver(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	@Override
	public Object resolve(IHibernateProxyDescriptor proxy) {
		Object entity;
		if(inProcessProxies.containsKey(proxy.getKey()))
		{
			entity = inProcessProxies.get(proxy.getKey());
		} else {
			try
			{
				Class<?> entityClass = (Class<?>) Class.forName(proxy.getRemoteClassName());
				Serializable identity = (Serializable) proxy.getProxyId();
				if (identity instanceof String)
				{
					identity = Integer.parseInt((String) identity);
				}
				entity = sessionFactory.getCurrentSession().get(entityClass, identity);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		return entity;
		
	}
	@Override
	public void addInProcessProxy(Object proxyKey, IHibernateProxy entity) {
		inProcessProxies.put(proxyKey, entity);
	}
	@Override
	public void removeInProcessProxy(Object proxyKey, IHibernateProxy entity) {
		inProcessProxies.remove(proxyKey);
	}

}
