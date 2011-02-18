package net.digitalprimates.persistence.state.testObjects
{
	import org.dphibernate.core.HibernateBean;
	
	
	[Managed]
	[RemoteClass(alias="net.digitalprimates.persistence.hibernate.testObjects.Book")]
	public class Book extends HibernateBean
	{
		public function Book()
		{
		}
		
		public var author : Author;
		public var title : String;
		
		[Transient]
		public var pages : int;
	}
}