package org.dphibernate.persistence.state;

import java.util.List;

import javax.annotation.PostConstruct;

import org.dphibernate.persistence.interceptors.IPostUpdateInterceptor;
import org.dphibernate.persistence.interceptors.IPreUpdateInterceptor;
import org.dphibernate.serialization.SerializerCache;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseSpringWiredObjectChangeUpdater extends ObjectChangeUpdater {

	@Autowired(required=false)
	private List<IPostUpdateInterceptor> autowiredPostProcessors;
	@Autowired(required=false)
	private List<IPreUpdateInterceptor> autowiredPreProcessors;
	
	@PostConstruct
	void onPostConstruct()
	{
		if (getPreProcessors() == null && autowiredPreProcessors != null)
			setPreProcessors(autowiredPreProcessors);
		
		if (getPostProcessors() == null && autowiredPostProcessors != null)
			setPostProcessors(autowiredPostProcessors);
	}

	public BaseSpringWiredObjectChangeUpdater(SessionFactory sessionFactory,
			IProxyResolver proxyResolver, SerializerCache cache) {
		super(sessionFactory,proxyResolver,cache);
	}

	public BaseSpringWiredObjectChangeUpdater() {
		super();
	}

	public BaseSpringWiredObjectChangeUpdater(SessionFactory sessionFactory,
			IProxyResolver proxyResolver) {
		super(sessionFactory,proxyResolver);
	}

}
