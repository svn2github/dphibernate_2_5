package org.dphibernate.persistence.state;

import java.security.Principal;

public interface IPrincipalProvider
{

	Principal getPrincipal();
	boolean isAnonymous();
}
