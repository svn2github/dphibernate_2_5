package org.dphibernate.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the class should be proxied
 * even if it has been fully retrieved from the database
 * (ie., is not a Hibernate proxy).
 * 
 * This is useful for optomizing serialization of large object graphs 
 * of objects which have already been fully loaded from the db. 
 * @author Marty
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface AggressivelyProxy
{

}
