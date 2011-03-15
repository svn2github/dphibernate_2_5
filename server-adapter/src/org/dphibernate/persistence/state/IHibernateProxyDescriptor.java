package org.dphibernate.persistence.state;

import org.dphibernate.core.IEntity;



public interface IHibernateProxyDescriptor {
	public String getRemoteClassName();
	public Object getProxyId();
	public void setProxyId(Object object);
	public String getKey();
	public boolean matches(IEntity entity);
	public boolean isForClass(Class<? extends IEntity> proxyClass);
}
