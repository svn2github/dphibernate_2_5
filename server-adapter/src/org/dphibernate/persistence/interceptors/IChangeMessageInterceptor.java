package org.dphibernate.persistence.interceptors;

import java.security.Principal;

import org.dphibernate.persistence.state.IProxyResolver;
import org.dphibernate.persistence.state.ObjectChangeMessage;


public interface IChangeMessageInterceptor
{
	public boolean appliesToMessage(ObjectChangeMessage message);
	public void processMessage(ObjectChangeMessage message,IProxyResolver proxyResolver);
	public void processMessage(ObjectChangeMessage message,IProxyResolver proxyResolver, Principal user);
}
