package org.dphibernate.adapters;

import org.dphibernate.operations.AdapterOperation;
import org.dphibernate.serialization.ISerializerFactory;

public interface IAdapter
{

	ISerializerFactory getSerializerFactory();
	void setSerializerFactory(ISerializerFactory serializerFactory);

	void putOperation(AdapterOperation operation);
}
