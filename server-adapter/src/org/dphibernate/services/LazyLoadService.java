package org.dphibernate.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dphibernate.persistence.state.ObjectChangeMessage;
import org.dphibernate.persistence.state.ObjectChangeResult;
import org.hibernate.SessionFactory;

/**
 * A general purpose DataAccess service which
 * facilitates proxy loading and proxy saving
 * through an IProxyLoadService and IProxyUpdateService respectively.
 * 
 * Can be configured with custom services, or initialized to defaults
 * by simply passing the SesssionFactory
 * @author Marty Pitt
 *
 */
public class LazyLoadService implements IProxyLoadService, IProxyBatchLoader {

	private final IProxyLoadService proxyLoadService;
	private final IProxyBatchLoader proxyBatchLoader;
	
	public LazyLoadService(SessionFactory sessionFactory,IProxyLoadService proxyLoadService,IProxyBatchLoader proxyBatchLoader)
	{
		this.proxyLoadService = proxyLoadService;
		this.proxyBatchLoader = proxyBatchLoader;
	}
	public LazyLoadService(SessionFactory sessionFactory)
	{
		proxyLoadService = new ProxyLoadService(sessionFactory);
		proxyBatchLoader = new ProxyBatchLoader(sessionFactory);
	}
	@Override
	public Object loadBean(Class daoClass, Serializable id) {
		return proxyLoadService.loadBean(daoClass, id);
	}
	@Override
	public Map<String, Object> loadProperties(Class<?> daoClass, Serializable id)
	{
		return proxyLoadService.loadProperties(daoClass,id);
	}
	@Override
	public List<ProxyLoadResult> loadProxyBatch(ProxyLoadRequest[] requests)
	{
		return proxyBatchLoader.loadProxyBatch(requests);
	}

}
