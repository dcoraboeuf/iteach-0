package net.iteach.service.security;

import java.util.Collection;

import net.iteach.core.security.SecurityUtils;
import net.iteach.core.security.User;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class DefaultSecurityUtils implements SecurityUtils {

	private static final User ANONYMOUS = new User() {
		
		@Override
		public boolean isAnonymous() {
			return true;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return AuthorityUtils.createAuthorityList();
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public String getUsername() {
			return null;
		}

		@Override
		public boolean isAccountNonExpired() {
			return false;
		}

		@Override
		public boolean isAccountNonLocked() {
			return false;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return false;
		}

		@Override
		public boolean isEnabled() {
			return false;
		}

		@Override
		public int getId() {
			return 0;
		}
	};

	@Override
	public boolean isLogged() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken));
	}
	
	@Override
	public int getCurrentUserId() {
		User user = getCurrentUser();
		if (user.isAnonymous()) {
			throw new AccessDeniedException("Anonymous user is denied access");
		} else {
			return user.getId();
		}
	}
	
	@Override
	public User getCurrentUser() {
		if (isLogged()) {
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.getPrincipal() instanceof User) {
				return (User) authentication.getPrincipal();
			} else {
				return ANONYMOUS;
			}
		} else {
			return ANONYMOUS;
		}
	}

}
