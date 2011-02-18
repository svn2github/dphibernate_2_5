import org.junit.runners.Suite.SuiteClasses;

import net.digitalprimates.persistence.hibernate.tests.SerializeManyToMany;
import net.digitalprimates.persistence.hibernate.tests.SerializeOneToMany;
import junit.framework.Test;
import junit.framework.TestSuite;

@SuiteClasses({SerializeManyToMany.class, SerializeOneToMany.class})
public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for net.digitalprimates.persistence.hibernate.tests");
		//$JUnit-BEGIN$
		//suite.addTest( new SerializeManyToMany() );
		//$JUnit-END$
		return suite;
	}

}
