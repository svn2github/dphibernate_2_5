package org.dphibernate.persistence.state;

import java.util.List;
import java.util.Set;

public interface IObjectChangeUpdater {
	public Set<ObjectChangeResult> update(ObjectChangeMessage changeMessage);

	public Set<ObjectChangeResult> update(
			List<ObjectChangeMessage> changeMessages);

	public List<ObjectChangeMessage> orderByDependencies(
			List<ObjectChangeMessage> objectChangeMessages);
}
