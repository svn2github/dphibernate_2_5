package org.dphibernate.persistence.state;

import java.util.List;
import java.util.Set;

public interface IDataAccessService
{
	public Set<ObjectChangeResult> saveBean(ObjectChangeMessage objectChangeMessage);
	public Set<ObjectChangeResult> saveBean(List<ObjectChangeMessage> objectChangeMessage);
}
