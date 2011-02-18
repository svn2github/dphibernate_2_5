package org.dphibernate.persistence.interceptors;

/**
 * A marker interface - ie., adds no functionality over the base IChangeMessageInterceptor.
 * 
 * However, it becomes useful in DI-style scenarios, where depending on 
 * a collection of IPreUpdateInterceptor is more meaningful than 
 * a collection of IChangeMessageInterceptor
 * @author owner
 *
 */
public interface IPreUpdateInterceptor extends IChangeMessageInterceptor
{

}
