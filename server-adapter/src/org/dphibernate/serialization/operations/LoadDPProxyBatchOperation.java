package org.dphibernate.serialization.operations;

import org.dphibernate.operations.AdapterOperation;

import flex.messaging.messages.RemotingMessage;

public class LoadDPProxyBatchOperation implements AdapterOperation
{
	private final String methodName;

	public LoadDPProxyBatchOperation(String methodName)
	{
		this.methodName = methodName;
	}
	@Override
	public boolean appliesForMessage(RemotingMessage message)
	{
		return "loadProxyBatch".equals(message.getOperation());
	}


	@Override
	public void execute(RemotingMessage message)
	{
		message.setOperation(methodName);
	}
	public String getMethodName()
	{
		return methodName;
	}

}
