package org.dphibernate.proxy;

/**
 * Returns the identity of an entity.
 * Abstracted to allow different implementations (eg., JPA, HibernateAnnotations, HibernateSession, etc)
 * @author Marty Pitt
 *
 */
public interface EntityIdentityProvider
{

	Object getId(Object entity);
}
