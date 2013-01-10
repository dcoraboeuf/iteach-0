package net.iteach.service.security

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import net.iteach.api.SecurityService;
import net.iteach.api.model.AuthenticationMode;
import net.iteach.api.support.InMemoryPost;
import net.iteach.test.AbstractIntegrationTest;

class AccountTest extends AbstractIntegrationTest {

	@Autowired
	SecurityService securityService
	
	@Autowired
	InMemoryPost post
	
	@Autowired
	PasswordEncoder encoder

	/**
	 * Tests the registration process for a user using {@link AuthenticationMode#password password} mode.
	 */
	@Test
	void registration_basic () {
		// Registration
		securityService.register(Locale.ENGLISH, AuthenticationMode.password, "", "Test1", "User", "user1@test.com", "tpwd")
		// Gets the in-memory port
		def message = post.getMessage("user1@test.com")
		assert message != null : "No message was sent"
		assert "iTeach - Validation of your account" == message.title
		// Gets the token
		def token = message.content.token
		assert StringUtils.isNotBlank(token)
		// Get the form to complete the registration
		def form = securityService.getRegistrationCompletionForm(token)
		assert form != null
		assert token == form.token
		assert "user1@test.com" == form.user.email
		assert "Test1" == form.user.firstName
		assert "User" == form.user.lastName
		// Password to validate the registration
		def pwd = encoder.encodePassword("tpwd", "user1@test.com")
		// FIXME Complete the registration
		// securityService.completeRegistration(form, pwd)
	}
}
