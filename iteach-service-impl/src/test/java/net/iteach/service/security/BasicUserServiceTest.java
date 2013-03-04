package net.iteach.service.security;

import net.iteach.core.security.User;
import net.iteach.test.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;

public class BasicUserServiceTest extends AbstractIntegrationTest {

    private BasicUserService service;

    @Test
    public void admin() {
        User details = service.loadUserByUsername("admin");
        assertNotNull(details);
        assertEquals("admin", details.getPassword());
        assertEquals("The Administrator", details.getUsername());
        assertEquals(AuthorityUtils.createAuthorityList("ROLE_TEACHER"), details.getAuthorities());
        assertTrue(details.isEnabled());
    }

    @Test
    public void user() {
        User details = service.loadUserByUsername("user");
        assertNotNull(details);
        assertEquals("user", details.getPassword());
        assertEquals("A User", details.getUsername());
        assertEquals(AuthorityUtils.createAuthorityList("ROLE_TEACHER"), details.getAuthorities());
        assertTrue(details.isEnabled());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void not_found() {
        service.loadUserByUsername("password.notfound");
    }

}
