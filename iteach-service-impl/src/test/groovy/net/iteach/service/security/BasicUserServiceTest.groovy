package net.iteach.service.security

import net.iteach.test.AbstractIntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UsernameNotFoundException

class BasicUserServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private BasicUserService service
	
	@Test
	void admin() {
		def details = service.loadUserByUsername("admin")
		assert details != null
		assert details.getPassword() == "admin"
		assert details.getUsername() == "The Administrator"
		assert details.getAuthorities() == AuthorityUtils.createAuthorityList("ROLE_TEACHER")
		assert details.isEnabled()
	}
	
	@Test
	void user() {
		def details = service.loadUserByUsername("user")
		assert details != null
		assert details.getPassword() == "user"
		assert details.getUsername() == "A User"
		assert details.getAuthorities() == AuthorityUtils.createAuthorityList("ROLE_TEACHER")
		assert details.isEnabled()
	}
	
	@Test(expected = UsernameNotFoundException.class)
	void not_found() {
		service.loadUserByUsername("password.notfound")
	}

}
