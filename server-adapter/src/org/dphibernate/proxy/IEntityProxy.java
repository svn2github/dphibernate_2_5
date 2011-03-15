package org.dphibernate.proxy;

import org.dphibernate.core.IEntity;

public interface IEntityProxy extends IEntity
{
	Object getTarget();
}
