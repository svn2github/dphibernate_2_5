package org.dphibernate.services;

import java.util.List;
import java.util.Set;

import org.dphibernate.persistence.state.DbProxyResolver;
import org.dphibernate.persistence.state.IObjectChangeUpdater;
import org.dphibernate.persistence.state.IProxyResolver;
import org.dphibernate.persistence.state.ObjectChangeMessage;
import org.dphibernate.persistence.state.ObjectChangeResult;
import org.dphibernate.persistence.state.ObjectChangeUpdater;
import org.hibernate.SessionFactory;

public class ProxyUpdaterService implements IProxyUpdateService
{
	private final SessionFactory sessionFactory;

	public ProxyUpdaterService(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Set<ObjectChangeResult> saveBean(List<ObjectChangeMessage> objectChangeMessage)
	{
		// A new changeUpdater is created for each incoming request.
		// This is by design, to keep changeUpdaters single-use.
		IObjectChangeUpdater changeUpdater = buildObjectChangeUpdater();
		return changeUpdater.update(objectChangeMessage);
	}
	@Override
	public Set<ObjectChangeResult> saveBean(ObjectChangeMessage objectChangeMessage)
	{
		// A new changeUpdater is created for each incoming request.
		// This is by design, to keep changeUpdaters single-use.
		IObjectChangeUpdater changeUpdater = buildObjectChangeUpdater();
		return changeUpdater.update(objectChangeMessage);
	}
	
	IObjectChangeUpdater buildObjectChangeUpdater()
	{
		IProxyResolver proxyResolver = buildProxyResolver();
		return new ObjectChangeUpdater(sessionFactory, proxyResolver);
	}
	IProxyResolver buildProxyResolver()
	{
		return new DbProxyResolver(sessionFactory);
	}
}
