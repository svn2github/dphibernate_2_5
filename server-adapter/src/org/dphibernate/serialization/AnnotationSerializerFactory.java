package org.dphibernate.serialization;

import java.io.File;
import java.io.InputStream;

import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Serializer factory which is configured for use with Hibernate Annotations /
 * JPA.
 * 
 * @author owner
 * 
 */
public class AnnotationSerializerFactory extends SimpleSerializationFactory
{
	public AnnotationSerializerFactory()
	{
		String s = "";
		s.concat("");
	}
	@Override
	protected void initalizeSessionFactory()
	{
		try
		{
			// Create the SessionFactory
//			InputStream configInputStream =  getClass().getResourceAsStream("/hibernate.cfg.xml");
			
			
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		} catch (Throwable ex)
		{
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}

	}

}
