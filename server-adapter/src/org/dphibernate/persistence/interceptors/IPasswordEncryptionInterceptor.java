package org.dphibernate.persistence.interceptors;


/**
 * Marker interface for PasswordEncryptionInterceptor.
 * Required for Spring's Transaction Manager
 * to be able to create a proxy of PasswordEncryptionManager 
 * @author Marty Pitt
 *
 */
public interface IPasswordEncryptionInterceptor extends IChangeMessageInterceptor
{

}
