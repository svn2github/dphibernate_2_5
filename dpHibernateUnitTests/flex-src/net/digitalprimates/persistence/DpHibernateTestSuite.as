package net.digitalprimates.persistence
{
	import net.digitalprimates.persistence.state.HibernateUpdaterTests;
	import org.dphibernate.persistence.state.StateRepositoryTests;
	import org.dphibernate.rpc.LoadProxyRequestBufferTests;

	[Suite]
	[RunWith("org.flexunit.runners.Suite")]
	public class DpHibernateTestSuite
	{
//		public var stateRepositoryTests:StateRepositoryTests;
//		public var hibernateUpdaterTests:HibernateUpdaterTests;
		public var loadBuffer:LoadProxyRequestBufferTests;
	}
}