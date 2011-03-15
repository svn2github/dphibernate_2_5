package org.dphibernate.persistence.interceptors;

import org.dphibernate.core.IEntity;
import org.hibernate.SessionFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * Convenience class, which exposes PasswordEncryprionInterceptor as a IPostUpdateInterceptor to allow
 * for easier spring wiring
 * @author Marty Pitt
 *
 */
public class PassowrdEncryptionPostUpdateInterceptor extends
		PasswordEncryptionInterceptor implements IPostUpdateInterceptor {

	public PassowrdEncryptionPostUpdateInterceptor(
			Class<? extends IEntity> entityClass,
			String passwordPropertyName, PasswordEncoder passwordEncoder,
			SessionFactory sessionFactory) {
		super(entityClass, passwordPropertyName, passwordEncoder, sessionFactory);
	}

}
