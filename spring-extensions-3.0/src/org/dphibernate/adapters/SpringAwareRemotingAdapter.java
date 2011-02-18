package org.dphibernate.adapters;

import org.dphibernate.spring.util.SpringContextHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringAwareRemotingAdapter extends RemotingAdapter implements ApplicationContextAware{

	private ApplicationContext applicationContext;
	private SpringContextHelper contextHelper;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
				this.applicationContext = applicationContext;
	}

	public void setContextHelper(SpringContextHelper contextHelper) {
		this.contextHelper = contextHelper;
	}

	public SpringContextHelper getContextHelper() {
		return contextHelper;
	}
	

}
