package org.dphibernate.persistence.state;

import org.dphibernate.core.IHibernateProxy;



public interface IHibernateProxyDescriptor {
	public String getRemoteClassName();
	public Object getProxyId();
	public void setProxyId(Object object);
	public String getKey();
	public boolean matches(IHibernateProxy entity);
	public boolean isForClass(Class<? extends IHibernateProxy> proxyClass);
}
