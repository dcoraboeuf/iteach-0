package net.iteach.core.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class DefaultSecurityUtils implements SecurityUtils {

	private static final User ANONYMOUS = new User() {
		
		@Override
		public boolean isAnonymous() {
			return true;
		}
		
		@Override
		public String getDisplayName() {
			return "";
		}
	};

	@Override
	public boolean isLogged() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken));
	}
	
	@Override
	public User getCurrentUser() {
		if (isLogged()) {
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return new User() {
				
				@Override
				public boolean isAnonymous() {
					return false;
				}
				
				@Override
				public String getDisplayName() {
					return authentication.getName();
				}
			};
		} else {
			return ANONYMOUS;
		}
	}

}
