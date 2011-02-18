package net.digitalprimates.persistence.state.testObjects
{
	import mx.collections.ArrayCollection;
	
	import org.dphibernate.core.HibernateBean;
	
	
	[Managed]
	[RemoteClass(alias="net.digitalprimates.persistence.hibernate.testObjects.Author")]
	public class Author extends HibernateBean
	{
		public function Author()
		{
		}
		
		public var name : String;
		public var age : int;
		public var books : ArrayCollection;
		public var publisher : Publisher;
	}
}