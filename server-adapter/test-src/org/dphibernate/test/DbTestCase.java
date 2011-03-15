package org.dphibernate.test;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dphibernate.model.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class DbTestCase
{
	private static final String JDBC_PASSWORD = "";
	private static final String JDBC_USERNAME = "sa";
	private static final String JDBC_CONNECTION_URL = "jdbc:hsqldb:mem:dphibernate";
	private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.HSQLDialect";
	private IDatabaseTester databaseTester;
	private static SessionFactory sessionFactory;

	Transaction transaction;
	protected File getDataSet() throws Exception
	{
		return null;
	}

	private static FlatXmlDataSet baseDataset;


	@BeforeClass
	public static void setupBaseDataset() throws Exception
	{
		baseDataset = new FlatXmlDataSetBuilder().build(getStream("BaseDataset.xml"));
		initializeHibernate();
	}


	private static InputStream getStream(String string)
	{
		return ClassLoader.getSystemResourceAsStream("BaseDataset.xml");
	}


	private static void initializeHibernate()
	{
		// See hibernate.cfg.xml for other config
		sessionFactory = new AnnotationConfiguration()
			.setProperty("hibernate.hbm2ddl.auto", "create-drop")
			.setProperty("hibernate.show_sql", "true")
			.configure()
			.buildSessionFactory();
	}


	@Before
	public void setupDbUnit() throws Exception
	{
		databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_CONNECTION_URL, JDBC_USERNAME, JDBC_PASSWORD);
		IDataSet dataSet = setupDataset();
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
		
		transaction = getCurrentSession().beginTransaction();
	}


	@After
	public void tearDownDbUnit() throws Exception
	{
		databaseTester.onTearDown();
	}


	protected IDataSet setupDataset() throws Exception
	{
		File subclassFile = getDataSet();
		CompositeDataSet dataset;
		if (subclassFile != null)
		{
			dataset = new CompositeDataSet(baseDataset, new FlatXmlDataSetBuilder().build(subclassFile));
		} else
		{
			dataset = new CompositeDataSet(baseDataset);
		}
		return dataset;
	}


	public SessionFactory getSessionFactory()
	{
		return DbTestCase.sessionFactory;
	}
	public Session getCurrentSession()
	{
		return getSessionFactory().getCurrentSession();
	}
	
	public <T extends BaseEntity> T get(Class<T> t,Serializable id)
	{
		return (T) getCurrentSession().get(t, id);
	}
	public <T extends BaseEntity> List<T> getList(Class<T> t,Serializable... ids)
	{
		Criteria criteria = getCurrentSession().createCriteria(t);
		criteria.add(Restrictions.in("id", ids));
		return criteria.list();
	}

}
