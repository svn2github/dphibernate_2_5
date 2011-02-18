package org.dphibernate.services;

public class ProxyLoadResult
{
	private String requestKey;
	private Object result;


	public ProxyLoadResult()
	{
	}


	public ProxyLoadResult(String requestKey, Object result)
	{
		this.requestKey = requestKey;
		this.result = result;

	}


	public void setRequestKey(String requestKey)
	{
		this.requestKey = requestKey;
	}


	public String getRequestKey()
	{
		return requestKey;
	}


	public void setResult(Object result)
	{
		this.result = result;
	}


	public Object getResult()
	{
		return result;
	}
	public boolean isForRequest(ProxyLoadRequest request)
	{
		return this.requestKey.equals(request.getRequestKey());
	}
}
