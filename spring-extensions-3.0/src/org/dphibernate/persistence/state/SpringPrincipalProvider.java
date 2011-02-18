package org.dphibernate.persistence.state;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringPrincipalProvider implements IPrincipalProvider {

	@Override
	public Principal getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof Principal)
		{
			return (Principal) authentication.getPrincipal();
		}
		return null;
		
	}

	@Override
	public boolean isAnonymous() {
		return getPrincipal() == null;
//		return getPrincipal().equals("roleAnonymous");
	}

}
