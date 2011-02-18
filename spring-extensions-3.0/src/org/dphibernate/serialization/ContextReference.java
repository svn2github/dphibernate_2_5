package org.dphibernate.serialization;

import org.springframework.context.ApplicationContext;

public class ContextReference {

	private final ApplicationContext context;
	private final String beanName;

	public ContextReference(String beanName,ApplicationContext context)
	{
		this.beanName = beanName;
		this.context = context;
		
	}

	public ApplicationContext getContext() {
		return context;
	}

	public String getBeanName() {
		return beanName;
	}
}
