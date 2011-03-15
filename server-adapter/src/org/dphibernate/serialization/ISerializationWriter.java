package org.dphibernate.serialization;

/**
 * Writers responsible for serializating a given type.
 * 
 * Serialization occurs in two phases:  Generation of a stub value,
 * then population of it.
 * 
 * This is to allow efficient caching of values prior to their serialization.
 * @author Marty Pitt
 *
 * @param <T>
 */
public interface ISerializationWriter<T,K>
{
	public void populateStub(T source, K stub);
	/**
	 * Returns a new unpopulated object
	 * that matches the root type that the serializer will 
	 * eventually reutrn.
	 * 
	 * Eg.,  for a list serializer, return an empty List<ASObject>.
	 * For an array serializer, return an empty  
	 * @return
	 */
	public K createStubValue(T source);
}
