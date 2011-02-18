package org.dphibernate.services;

import java.io.Serializable;
import java.util.Map;

public interface IProxyLoadService
{
	Object loadBean(Class daoClass, Serializable id);

	Map<String, Object> loadProperties(Class<?> daoClass, Serializable id);
}
