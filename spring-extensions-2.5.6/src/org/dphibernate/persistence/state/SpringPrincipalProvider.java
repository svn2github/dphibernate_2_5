package org.dphibernate.persistence.state;

import java.security.Principal;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

public class SpringPrincipalProvider implements IPrincipalProvider {

	@Override
	public Principal getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal.equals("roleAnonymous"))
		{
			return new AnonymousPrincipal();
		} else if (principal.getClass().isAssignableFrom(Principal.class)) {
			return (Principal) principal;
		} else {
			// TODO : Log a warning!
			System.out.print("WARNING : Authentication returned from SecurityContext does not implement java.security.Principal.  Treated as Anonymous");
			return new AnonymousPrincipal();
		}
	}

	@Override
	public boolean isAnonymous() {
		return getPrincipal().getClass().equals(AnonymousPrincipal.class);
	}
	class AnonymousPrincipal implements Principal
	{

		@Override
		public String getName() {
			return "Anonymous";
		}
		
	}

}
