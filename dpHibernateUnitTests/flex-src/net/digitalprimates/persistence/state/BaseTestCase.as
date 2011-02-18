package net.digitalprimates.persistence.state
{
	import mx.collections.ArrayCollection;
	
	import net.digitalprimates.persistence.state.testObjects.Author;
	import net.digitalprimates.persistence.state.testObjects.Book;
	import net.digitalprimates.persistence.state.testObjects.Publisher;
	
	import org.dphibernate.persistence.state.StateRepository;
	import org.dphibernate.rpc.HibernateManaged;
	import org.dphibernate.rpc.IHibernateRPC;
	import org.mockito.integrations.mock;
	
	[RunWith("org.mockito.integrations.flexunit4.MockitoClassRunner")]
	public class BaseTestCase
	{
		[Mock]
		public var mockService:IHibernateRPC;

		[Before]
		public function setUp():void
		{
			StateRepository.reset();
			mockService=mock(IHibernateRPC);
			HibernateManaged.defaultHibernateService = mockService;
		}

		// Data Helpers
		/**
		 * Returns a populated Author class as if it had come across the wire
		 * */
		public function getTestAuthor(service:IHibernateRPC=null):Author
		{
			if (!service)
				service=mockService;
			var author:Author=new Author();
			author.name="Bloch";
			author.age=45;
			author.publisher=getPublisher();
			author.books=new ArrayCollection([getBook(1, "Effective Java", author), getBook(2, "Effective Java 2nd Edition", author)]);

			author.proxyKey=123;
			author.proxyInitialized=true;

			HibernateManaged.manageHibernateObject(author, null);

			return author;
		}

		public function getTestAuthorProxy(service:IHibernateRPC=null):Author
		{
			if (!service)
				service=mockService;
			var author:Author=new Author();
			author.proxyKey=123;
			author.proxyInitialized=false;
			HibernateManaged.manageHibernateObject(author, null);
			return author;
		}

		public function getBook(id:int, title:String, author:Author):Book
		{
			var book:Book=getNewBook(title, author);
			book.proxyKey=id;
			return book;
		}

		public function getNewBook(title:String, author:Author):Book
		{
			var book:Book=new Book();
			book.author=author;
			book.title=title;
			return book;
		}

		public function getPublisher():Publisher
		{
			var publisher:Publisher=new Publisher();
			publisher.address="123 Somewhere Lane";
			publisher.name="Addison Wesley";
			return publisher;
		}


	}
}