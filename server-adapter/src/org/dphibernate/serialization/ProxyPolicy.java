package org.dphibernate.serialization;

public interface ProxyPolicy
{
	boolean shouldAggressivelyProxy(Object source,boolean eagerlySerialize);
}
