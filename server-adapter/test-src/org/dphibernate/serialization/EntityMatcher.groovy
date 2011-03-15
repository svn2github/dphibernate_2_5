package org.dphibernate.serialization

import flex.messaging.io.amf.ASObject;

class EntityMatcher
{
	String propertyName;
	ASObject source
	boolean lazy
	
	EntityMatcher(ASObject source, String propertyName, boolean lazy)
	{
		this.source = source
		this.propertyName = propertyName
		this.lazy = lazy
	}
	def matching(Object target)
	{
		def property = source.get(propertyName)
		assert property
		assert property.isForEntity(target)
		return true
	}
}
