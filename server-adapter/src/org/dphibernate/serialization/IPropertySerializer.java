package org.dphibernate.serialization;

/**
 * A serializer which can serve many objects (as opposed to an ISerializer,
 * which is designed to be per-single-root object.
 * 
 * Also, PropertySerialiers would generally inherit their serializationCache
 * from the root ISerializer.
 * 
 * In practice, an IPropertySerializer is often the same object as an
 * ISerializer.  This interface may need revisiting
 * 
 * @author Marty Pitt
 *
 */
public interface IPropertySerializer
{
	Object serialize(Object source);
	Object serialize(Object source, SerializationMode serializationMode);
}
