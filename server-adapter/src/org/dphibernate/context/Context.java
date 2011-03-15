package org.dphibernate.context;

import org.dphibernate.proxy.EntityProxyFactory;
import org.dphibernate.serialization.ISerializerFactory;
import org.dphibernate.serialization.SerializerConfiguration;
import org.dphibernate.serialization.WriterFactory;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;

public interface Context extends ISerializerFactory
{

	WriterFactory getWriterFactory();
	SessionFactory getSessionFactory();
	Dialect getDialect();
	ISerializerFactory getSerializerFactory();
	EntityProxyFactory getProxyFactory();
	SerializerConfiguration getDefaultConfiguration();
	

}
