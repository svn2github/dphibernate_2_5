package org.dphibernate.persistence.state;

import org.dphibernate.core.IEntity;

public interface IProxyResolver {
	public Object resolve(IHibernateProxyDescriptor proxy);

	public void addInProcessProxy(Object proxyKey, IEntity entity);
	public void removeInProcessProxy(Object proxyKey, IEntity entity);
	
}
