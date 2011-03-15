package org.dphibernate.model
{
	import mx.collections.ArrayCollection;
	
	import org.dphibernate.core.BaseEntity;
	
	
	[RemoteClass(alias="net.digitalprimates.persistence.hibernate.testObjects.Author")]
	public class Author extends BaseEntity
	{
		public function Author(id:int)
		{
			this.entityKey = id;
		}
		
		public var name : String;
		public var age : int;
		public var books : ArrayCollection;
		public var publisher : Publisher;
		
		public static function createWithBooks(id:int,books:Array):Author
		{
			var author:Author = new Author(id);
			author.books = new ArrayCollection(books);
			return author;
		}
		
		public function getBookAt(index:int):Book
		{
			return books.getItemAt(index) as Book;
		}
	}
}