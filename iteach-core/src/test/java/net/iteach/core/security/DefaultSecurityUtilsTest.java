package net.iteach.core.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class DefaultSecurityUtilsTest {
	
	private final DefaultSecurityUtils utils = new DefaultSecurityUtils();
	
	@Test
	public void no_context() {
		SecurityContextHolder.clearContext();
		assertFalse("Empty security context must be flagged as not logged", utils.isLogged());
	}
	
	@Test
	public void not_authenticated() {
		Authentication authentication = mock (Authentication.class);
		when(authentication.isAuthenticated()).thenReturn(false);
		SecurityContext context = mock(SecurityContext.class);
		when(context.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(context);
		assertFalse("Not authenticated authentication must be flagged as not logged", utils.isLogged());
	}
	
	@Test
	public void authenticated() {
		Authentication authentication = mock (Authentication.class);
		when(authentication.isAuthenticated()).thenReturn(true);
		SecurityContext context = mock(SecurityContext.class);
		when(context.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(context);
		assertTrue("Authenticated authentication must be flagged as logged", utils.isLogged());
	}
	
	@Test
	public void anonymous() {
		Authentication authentication = new AnonymousAuthenticationToken("key", "principal", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
		SecurityContext context = mock(SecurityContext.class);
		when(context.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(context);
		assertFalse("Anonymous authentication must be flagged as not logged", utils.isLogged());
	}

}
