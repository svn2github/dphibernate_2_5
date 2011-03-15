package org.dphibernate.model
{
	import org.dphibernate.core.BaseEntity;
	
	
	[RemoteClass(alias="remote.Book")]
	public class Book extends BaseEntity
	{
		public function Book(id:int)
		{
			this.entityKey = id;
		}
		// Array of simple objects (Strings)
		public var chapters:Array; // Of String
		public var author : Author;
		public var title : String;
		
		[Transient]
		public var pages : int;
		
		// Test read-only property
		public function get amazonRating():int
		{
			return 5;
		}
		
		public static function createWithTitle(id:int,title:String):Book
		{
			var book:Book = new Book(id);
			book.title = title;
			return book;
		}
		public static function createWithChapters(id:int,chapters:Array):Book
		{
			var book:Book = new Book(id);
			book.chapters = chapters;
			return book;
		}
	}
}