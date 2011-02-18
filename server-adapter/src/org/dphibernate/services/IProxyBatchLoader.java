package org.dphibernate.services;

import java.util.List;

public interface IProxyBatchLoader
{
	 List<ProxyLoadResult> loadProxyBatch(ProxyLoadRequest[] requests);
}
