package org.dphibernate.services;

import java.util.List;
import java.util.Set;

import org.dphibernate.persistence.state.ObjectChangeMessage;
import org.dphibernate.persistence.state.ObjectChangeResult;


public interface IProxyUpdateService
{
	Set<ObjectChangeResult> saveBean(List<ObjectChangeMessage> objectChangeMessage);
	Set<ObjectChangeResult> saveBean(ObjectChangeMessage objectChangeMessage);
}
