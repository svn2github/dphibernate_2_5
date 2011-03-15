package org.dphibernate.model;

public class TwoArraysOfBooks
{
	private Book[] bookList1;
	private Book[] bookList2;


	public TwoArraysOfBooks(Book[] list1, Book[] list2)
	{
		this.setBookList1(bookList1);
		this.setBookList2(bookList2);
	}


	public void setBookList1(Book[] bookList1)
	{
		this.bookList1 = bookList1;
	}


	public Book[] getBookList1()
	{
		return bookList1;
	}


	public void setBookList2(Book[] bookList2)
	{
		this.bookList2 = bookList2;
	}


	public Book[] getBookList2()
	{
		return bookList2;
	}

}
