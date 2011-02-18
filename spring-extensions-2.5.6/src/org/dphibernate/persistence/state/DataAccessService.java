package org.dphibernate.persistence.state;

import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * An example of a dataAccessService to facilitate saving of beans
 * through dpHibernate.
 * 
 * Note that generally, the service is scoped to Singleton in Spring (though this
 * isn't a requirement).  However, the ObjectChangeUpdater MUST be scoped to Prototype.
 * For this reason, to ensure a new objectChangeUpdater is used for each call, this class
 * implements BeanFactoryAware, and fetches an objectchangeUpdater as required.
 * @author Marty
 *
 */
@Service
@Transactional
public class DataAccessService implements IDataAccessService, BeanFactoryAware
{

	private BeanFactory beanFactory;
	@Transactional(readOnly=false)
	@Override
	public Set<ObjectChangeResult> saveBean(List<ObjectChangeMessage> objectChangeMessage)
	{
		return getObjectChangeUpdater().update(objectChangeMessage);
	}
	@Transactional(readOnly=false)
	@Override
	public Set<ObjectChangeResult> saveBean(ObjectChangeMessage objectChangeMessage)
	{
		return getObjectChangeUpdater().update(objectChangeMessage);
	}
	
	private IObjectChangeUpdater getObjectChangeUpdater()
	{
		// Access the objectChangeUpdater through the bean factory as it should
		// have it's scope set to prototype.  This dataAccessService however is generally
		// a singleton.
		IObjectChangeUpdater updater = (IObjectChangeUpdater) beanFactory.getBean("objectChangeUpdater");
		return updater;
	}
	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException
	{
		this.beanFactory = arg0;
	}

}
