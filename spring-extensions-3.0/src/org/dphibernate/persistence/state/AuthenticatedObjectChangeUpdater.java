package org.dphibernate.persistence.state;

import org.dphibernate.serialization.DPHibernateCache;
import org.hibernate.SessionFactory;
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
			IProxyResolver proxyResolver, DPHibernateCache cache) {
		super(sessionFactory, proxyResolver, cache);
		setPrincipleProvider(new SpringPrincipalProvider());
	}

	public AuthenticatedObjectChangeUpdater(SessionFactory sessionFactory,
			IProxyResolver proxyResolver) {
		super(sessionFactory, proxyResolver);
		setPrincipleProvider(new SpringPrincipalProvider());
	}

}
