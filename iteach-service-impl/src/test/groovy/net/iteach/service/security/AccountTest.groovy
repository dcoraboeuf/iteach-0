package net.iteach.service.security

import net.iteach.api.SecurityService
import net.iteach.api.support.InMemoryPost
import net.iteach.core.model.AuthenticationMode
import net.iteach.test.AbstractIntegrationTest
import org.apache.commons.lang3.StringUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class AccountTest extends AbstractIntegrationTest {

	@Autowired
	SecurityService securityService
	
	@Autowired
	InMemoryPost post
	
	@Autowired
	PasswordEncoder passwordEncoder
	
	@Autowired
	UserDetailsService basicUserService

	/**
	 * Tests the registration process for a user using {@link AuthenticationMode#password password} mode.
	 */
	@Test
	void registration_basic () {
		
		// Checks we cannot login
		assertBasicLoginKO("user1@test.com", "tpwd")
		
		// Registration
		securityService.register(Locale.ENGLISH, AuthenticationMode.password, "", "Test1", "User", "user1@test.com", "tpwd")
		
		// Checks we still cannot login
		assertBasicLoginKO("user1@test.com", "tpwd")
		
		// Gets the in-memory port
		def message = post.getMessage("user1@test.com")
		assert message != null : "No message was sent"
		assert "iTeach - Validation of your account" == message.title
		// Gets the token
		def token = message.content.token
		assert StringUtils.isNotBlank(token)
		// Complete the registration
		def ack = securityService.completeRegistration(token)
		assert ack != null && ack.success
		
		// Checks we can now login
		assertBasicLoginOK("user1@test.com", "tpwd")
	}
	
	def assertBasicLoginKO (String email, String password) {
		try {
			basicUserService.loadUserByUsername(email)
			assert false : "The user ${email} must not be able to log"
		} catch (UsernameNotFoundException ex) {
			// OK, not found
		}
	}
	
	def assertBasicLoginOK (String email, String password) {
		def user = basicUserService.loadUserByUsername(email)
		assert user != null
		assert passwordEncoder.encodePassword(password, email) == user.getPassword()
	}
}

