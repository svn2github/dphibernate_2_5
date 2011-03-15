package org.dphibernate.proxy;

import java.lang.reflect.Method;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.proxy.Interceptor;
import org.apache.commons.proxy.Invocation;
import org.dphibernate.core.IEntity;
import org.dphibernate.serialization.annotations.NeverSerialize;
import org.dphibernate.utils.EntityUtil;
import org.dphibernate.utils.HibernateSessionManager;

/**
 * MethodInterceptor which proxies any Hibernate / JPA
 * class into a DPHibernate entity
 * @author Marty Pitt
 *
 */
public class EntityMethodInterceptor implements Interceptor
{
	private final EntityIdentityProvider entityIdentityProvider;

	public EntityMethodInterceptor(EntityIdentityProvider entityIdentityProvider)
	{
		this.entityIdentityProvider = entityIdentityProvider;
	}
	@Override
	public Object intercept(Invocation invocation) throws Throwable
	{
		Class<?> declaringClass = invocation.getMethod().getDeclaringClass();
		if  (declaringClass.equals(IEntity.class) || declaringClass.equals(IEntityProxy.class))
		{
			return interceptEntityProxyInvocation(invocation);
		} else {
			return invocation.proceed();
		}
	}

	private Object interceptEntityProxyInvocation(Invocation invocation)
	{
		Method method = invocation.getMethod();
		Object result;
		try
		{
			EntityProxyImpl entityProxyImpl = new EntityProxyImpl(invocation.getProxy(),entityIdentityProvider);
			result = method.invoke(entityProxyImpl, invocation.getArguments());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return result;
	}
}
class EntityProxyImpl implements IEntity, IEntityProxy
{
	private Boolean entityInitialized;
	private final Object entity;
	private final EntityIdentityProvider identityProvider;

	EntityProxyImpl(Object entity,EntityIdentityProvider identityProvider)
	{
		this.entity = entity;
		this.identityProvider = identityProvider;
		
	}
	@Override
	@NeverSerialize
	public Object getEntityKey()
	{
		return identityProvider.getId(entity);
	}

	@Override
	@NeverSerialize
	public Boolean getEntityInitialized()
	{
		return (entityInitialized != null) ?
			entityInitialized :
			!EntityUtil.isLazyProxy(entity);
	}
	@Override
	public void setEntityInitialized(Boolean b)
	{
		this.entityInitialized = b;
	}
	@Override
	public void setEntityKey(Object obj)
	{
		// SHould we allow this?  I suspect not.
		throw new NotImplementedException();
	}
	@Override
	@NeverSerialize
	public Object getTarget()
	{
		return entity;
	}
}
