package org.dphibernate.services;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


import org.dphibernate.model.Book;
import org.dphibernate.services.ProxyBatchLoader;
import org.dphibernate.services.ProxyLoadRequest;
import org.dphibernate.services.ProxyLoadResult;
import org.dphibernate.test.DbTestCase;
import org.junit.Before;
import org.junit.Test;

public class ProxyBatchLoaderTests extends DbTestCase
{
	
	private ProxyBatchLoader loader;

	@Before
	public void setup()
	{
		loader = new ProxyBatchLoader(getSessionFactory());
	}
	@Test
	public void testConvertingRequestsToMapByClass()
	{
		ProxyLoadRequest[] requests = {new ProxyLoadRequest("classA", 1),new ProxyLoadRequest("classA", 2),new ProxyLoadRequest("classB", 1)}; 
		Map<String, Collection<Serializable>> requestsByClass = loader.getRequestsByClass(requests);
		assertEquals(2,requestsByClass.keySet().size());
		assertEquals(2,requestsByClass.get("classA").size());
		assertEquals(1,requestsByClass.get("classB").size());
	}
	
	@Test
	public void returnsProxyLoadResult()
	{
		ProxyLoadRequest[] requests = ProxyLoadRequestBuilder.forEntity(Book.class).withIds(1,2,3);
		List<ProxyLoadResult> results = loader.loadProxyBatch(requests);
		assertContainsAll(results,requests);
	}
	
	@Test
	public void mapsResultsToOriginalRequest()
	{
		ProxyLoadRequest[] requests = ProxyLoadRequestBuilder.forEntity(Book.class).withIds(1,2,3);
		List<Book> books = getList(Book.class, 1,2,3);
		List<ProxyLoadResult> mappedResults = loader.mapLoadedEntitesToOriginalRequest(books, requests);
		assertEquals(3, mappedResults.size());
		assertContainsAll(mappedResults, requests);
	}
	
	
	// Custom asserts.....
	private void assertContainsAll(List<ProxyLoadResult> results, ProxyLoadRequest[] requests)
	{
		for (ProxyLoadRequest request:requests)
		{
			assertContains(results,request);
		}
	}
	private void assertContains(List<ProxyLoadResult> results, ProxyLoadRequest request)
	{
		ProxyLoadResult result = selectUnique(results,having(on(ProxyLoadResult.class).isForRequest(request)));
		assertNotNull(result);
	}
}
