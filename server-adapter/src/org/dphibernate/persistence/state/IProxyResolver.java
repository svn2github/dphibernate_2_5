package org.dphibernate.persistence.state;

import org.dphibernate.core.IHibernateProxy;

public interface IProxyResolver {
	public Object resolve(IHibernateProxyDescriptor proxy);

	public void addInProcessProxy(Object proxyKey, IHibernateProxy entity);
	public void removeInProcessProxy(Object proxyKey, IHibernateProxy entity);
	
}
