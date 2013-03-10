package net.iteach.service.security;

import net.iteach.api.UserNonVerifiedOrDisabledException;
import net.iteach.test.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;

public class OpenIDUserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private UserDetailsService openIDUserService;

    @Test
    public void ok() {
        UserDetails details = openIDUserService.loadUserByUsername("test.openid");
        assertNotNull(details);
        assertEquals("", details.getPassword());
        assertEquals("Mr John", details.getUsername());
        assertEquals(AuthorityUtils.createAuthorityList("ROLE_TEACHER"), details.getAuthorities());
        assertTrue(details.isEnabled());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void not_found() {
        openIDUserService.loadUserByUsername("test.notfound");
    }

    @Test(expected = UserNonVerifiedOrDisabledException.class)
    public void disabled() {
        openIDUserService.loadUserByUsername("disabled.openid");
    }

}
