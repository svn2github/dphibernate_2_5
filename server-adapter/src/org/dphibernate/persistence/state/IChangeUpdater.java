package org.dphibernate.persistence.state;

import java.util.List;


public interface IChangeUpdater {

	public List<ObjectChangeResult> update();
}
