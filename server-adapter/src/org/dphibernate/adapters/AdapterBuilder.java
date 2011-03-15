package org.dphibernate.adapters;

import java.util.HashMap;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dphibernate.operations.AdapterOperation;
import org.dphibernate.persistence.operations.SaveDPProxyOperation;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerConfiguration;
import org.dphibernate.serialization.SimpleSerializationFactory;
import org.dphibernate.serialization.operations.LoadDPProxyBatchOperation;
import org.dphibernate.serialization.operations.LoadDPProxyOperation;
import org.dphibernate.utils.HibernateSessionManager;

import flex.messaging.config.ConfigMap;

public class AdapterBuilder
{
	private static final Log log = LogFactory.getLog(AdapterBuilder.class);
	private static final String DEFAULT_LOAD_BATCH_METHOD_NAME = "loadProxyBatch";
	private static final String DEFAULT_LOAD_METHOD_NAME = "loadBean";
	private static final String DEFAULT_SAVE_METHOD_NAME = "saveBean";
	private static final String DEFAULT_SERIALZIER_FACTORY_CLASSNAME = SimpleSerializationFactory.class.getCanonicalName();

	private final IAdapter adapter;
	
	private String loadMethodName;
	private String saveMethodName;
	private String loadBatchMethodName;
	private SerializerConfiguration defaultConfiguration;
	private int pageSize = -1;
	
	public AdapterBuilder(IAdapter adapter)
	{
		this.adapter = adapter;
		
	}
	public void build(ConfigMap properties)
	{
		if (properties == null || properties.size() == 0)
			return;

		ConfigMap dpHibernateProps = properties.getPropertyAsMap("dpHibernate", new ConfigMap());
		loadMethodName = dpHibernateProps.getPropertyAsString("loadMethod", getDefaultLoadMethodName());
		loadBatchMethodName = dpHibernateProps.getPropertyAsString("loadBatchMethod", getDefaultLoadBatchMethodName());
		saveMethodName = dpHibernateProps.getPropertyAsString("saveMethod", getDefaultSaveMethodName());
		pageSize = dpHibernateProps.getPropertyAsInt("pageSize", getDefaultPageSize());
		adapter.putOperation(new LoadDPProxyOperation(loadMethodName));
		adapter.putOperation(new SaveDPProxyOperation(saveMethodName));
		adapter.putOperation(new LoadDPProxyBatchOperation(loadBatchMethodName));
		initializeDefaultConfiguration();
		initalizeSerializerFactory(dpHibernateProps);
		logConfiguration();
	}
	private String getDefaultLoadMethodName()
	{
		return (loadMethodName != null) ? loadMethodName : DEFAULT_LOAD_METHOD_NAME;
	}
	private String getDefaultLoadBatchMethodName()
	{
		return (loadBatchMethodName != null) ? loadBatchMethodName : DEFAULT_LOAD_BATCH_METHOD_NAME;
	}
	private String getDefaultSaveMethodName()
	{
		return (saveMethodName != null) ? saveMethodName : DEFAULT_SAVE_METHOD_NAME; 
	}
	private int getDefaultPageSize()
	{
		return (pageSize != -1) ? pageSize : -1;  
	}
	private void initializeDefaultConfiguration()
	{
		// Need to be using contextBuilder here
		throw new NotImplementedException();
	}
	private void initalizeSerializerFactory(ConfigMap adapterProps)
	{
		String serializationFactoryClassName = adapterProps.getPropertyAsString("serializerFactory", null);
		if (adapter.getSerializerFactory() != null && serializationFactoryClassName == null)
		{
			// The configuration did not specify an overriding SerializerFactory, and
			// we already have one configured, so exit now.
			return;
		}
		if (serializationFactoryClassName == null)
		{
			serializationFactoryClassName = DEFAULT_SERIALZIER_FACTORY_CLASSNAME;
		}
		Class<ISerializerFactory> serializationFactoryClass;
		try
		{
			serializationFactoryClass = (Class<ISerializerFactory>) Class.forName(serializationFactoryClassName);
			ISerializerFactory serializerFactory = serializationFactoryClass.newInstance();
			
			// TODO : This hsould use a context builder
//			serializerFactory.setDefaultConfiguration(defaultConfiguration);
			adapter.setSerializerFactory(serializerFactory);

			// TODO : Need to build our context
			throw new NotImplementedException();
			// TODO : Static reference?  This is a problem...
//			HibernateSessionManager.setSerializerFactory(serializerFactory);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	private void logConfiguration()
	{
		log.debug("dpHibernate loadMethodName: " + loadMethodName);
		log.debug("dpHibernate saveMethodName: " + saveMethodName);
		ISerializerFactory serializerFactory = adapter.getSerializerFactory();
		String serializerFactoryName = (serializerFactory != null) ? serializerFactory.getClass().getCanonicalName() : "undefined";
		log.debug("dpHibernate serializerFactory: " + serializerFactoryName);
	}

}
