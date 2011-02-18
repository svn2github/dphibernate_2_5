package org.dphibernate.collections;

import java.util.Collection;

import flex.messaging.io.ArrayCollection;

public class PaginatedCollection extends ArrayCollection
{

	public PaginatedCollection()
	{
		super();
	}

	public PaginatedCollection(Collection c)
	{
		super(c);
	}

	public PaginatedCollection(int initialCapacity)
	{
		super(initialCapacity);
	}

}
