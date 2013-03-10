package net.iteach.service.security;

import net.iteach.api.SecurityService;
import net.iteach.api.support.InMemoryPost;
import net.iteach.core.model.Ack;
import net.iteach.core.model.AuthenticationMode;
import net.iteach.core.model.Message;
import net.iteach.test.AbstractIntegrationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Locale;

import static org.junit.Assert.*;

public class AccountTest extends AbstractIntegrationTest {

    @Autowired
    public SecurityService securityService;

    @Autowired
    public InMemoryPost post;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsService basicUserService;

    /**
     * Tests the registration process for a user using {@link AuthenticationMode#password password} mode.
     */
    @Test
    public void registration_basic() {

        // Checks we cannot login
        assertBasicLoginKO("user1@test.com");

        // Registration
        securityService.register(Locale.ENGLISH, AuthenticationMode.password, "", "Test1", "User", "user1@test.com", "tpwd");

        // Checks we still cannot login
        assertBasicLoginKO("user1@test.com");

        // Gets the in-memory port
        Message message = post.getMessage("user1@test.com");
        assertNotNull("No message was sent", message);
        assertEquals("iTeach - Validation of your account", message.getTitle());
        // Gets the token
        String token = message.getContent().getToken();
        assertTrue(StringUtils.isNotBlank(token));
        // Complete the registration
        Ack ack = securityService.completeRegistration(token);
        assertNotNull(ack);
        assertTrue(ack.isSuccess());

        // Checks we can now login
        assertBasicLoginOK("user1@test.com", "tpwd");
    }

    protected void assertBasicLoginKO(String email) {
        try {
            basicUserService.loadUserByUsername(email);
            fail(String.format("The user %s must not be able to log", email));
        } catch (UsernameNotFoundException ex) {
            // OK, not found
        }
    }

    protected void assertBasicLoginOK(String email, String password) {
        UserDetails user = basicUserService.loadUserByUsername(email);
        assertNotNull(user);
        assertEquals(passwordEncoder.encodePassword(password, email), user.getPassword());
    }
}

