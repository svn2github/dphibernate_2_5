package org.dphibernate.serialization

import java.util.Collection;

import org.dphibernate.core.HibernateProxyConstants;

import flex.messaging.io.amf.ASObject;

class CollectionMatcher {
	ASObject source
	String propertyName
	boolean lazy
	
	CollectionMatcher(ASObject source,String propertyName,boolean lazy) {
		this.source = source;
		this.propertyName = propertyName;
		this.lazy = lazy
	}
	def matching(Collection<? extends Object> target) {
		List list = source.get(propertyName)
		assert list 
		assert list.size() == target.size()
		target.each { assert list.hasLazyEntity(it) }
	}
}
