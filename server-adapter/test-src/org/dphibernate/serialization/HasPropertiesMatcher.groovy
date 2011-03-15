package org.dphibernate.serialization

import flex.messaging.io.amf.ASObject;

class HasPropertiesMatcher
{
	String[] fieldsToMatch;
	ASObject source

	HasPropertiesMatcher(ASObject source, String fieldToMatch)
	{
		this (source, [fieldToMatch])
	}
	public HasPropertiesMatcher(ASObject source, List<String> fieldsToMatch)
	{
		this.source = source
		this.fieldsToMatch = fieldsToMatch
	}
	
	def from(Object target)
	{
		fieldsToMatch.each {
			assert source[it] == target[it]
		}
		return true;
	}
	
	
}
