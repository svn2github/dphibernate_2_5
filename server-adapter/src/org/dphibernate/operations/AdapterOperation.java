package org.dphibernate.operations;

import flex.messaging.messages.RemotingMessage;

public interface AdapterOperation {
	public void execute(RemotingMessage message);
	public boolean appliesForMessage(RemotingMessage message);
}
