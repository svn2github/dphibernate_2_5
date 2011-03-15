package org.dphibernate.persistence.state;

import java.util.List;
import java.util.Set;

import org.dphibernate.persistence.interceptors.IPostUpdateInterceptor;
import org.dphibernate.serialization.SerializerCache;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *  Extends ObjectChangeUpater to provide integration with Spring Security for providing
 *  Principals 
 */
@Transactional
public class AuthenticatedObjectChangeUpdater extends BaseSpringWiredObjectChangeUpdater
{

	public AuthenticatedObjectChangeUpdater() {
		super();
		setPrincipleProvider(new SpringPrincipalProvider());
	}

	public AuthenticatedObjectChangeUpdater(SessionFactory sessionFactory,
			IProxyResolver proxyResolver, SerializerCache cache) {
		super(sessionFactory, proxyResolver, cache);
		setPrincipleProvider(new SpringPrincipalProvider());
	}

	public AuthenticatedObjectChangeUpdater(SessionFactory sessionFactory,
			IProxyResolver proxyResolver) {
		super(sessionFactory, proxyResolver);
		setPrincipleProvider(new SpringPrincipalProvider());
	}
	
	@Override
	@Transactional(readOnly=false)
	public Set<ObjectChangeResult> update(List<ObjectChangeMessage> changeMessages)
	{
		return super.update(changeMessages);
	}
}
