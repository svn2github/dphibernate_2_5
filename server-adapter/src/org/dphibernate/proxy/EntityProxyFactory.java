package org.dphibernate.proxy;

import org.apache.commons.proxy.factory.javassist.JavassistProxyFactory;
import org.dphibernate.core.IEntity;

public class EntityProxyFactory
{

	private final EntityIdentityProvider identityProvider;
	public EntityProxyFactory(EntityIdentityProvider identityProvider)
	{
		this.identityProvider = identityProvider;
	}
	public IEntityProxy buildProxy(Object entity)
	{
		JavassistProxyFactory proxyFactory = new JavassistProxyFactory();
		EntityMethodInterceptor interceptor = new EntityMethodInterceptor(identityProvider);
		
		Object proxy = proxyFactory.createInterceptorProxy(entity, interceptor, new Class[]{entity.getClass(),IEntity.class,IEntityProxy.class});
		return (IEntityProxy) proxy;
	}

}
