package org.dphibernate.persistence.operations;

import org.dphibernate.operations.AdapterOperation;

import flex.messaging.messages.RemotingMessage;

public class SaveDPProxyOperation implements AdapterOperation {

	private final String saveMethodName;
	public SaveDPProxyOperation(String saveMethodName)
	{
		this.saveMethodName = saveMethodName;
	}
	public boolean appliesForMessage(RemotingMessage message) {
		return "saveDPProxy".equals(message.getOperation());
	}

	public void execute(RemotingMessage remotingMessage) {
		remotingMessage.setOperation(getSaveMethodName());
	}
	public String getSaveMethodName()
	{
		return saveMethodName;
	}

}
