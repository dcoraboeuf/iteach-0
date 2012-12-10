package net.iteach.service.security

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.iteach.api.SchoolService;
import net.iteach.test.AbstractIntegrationTest;

class OpenIDUserServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private OpenIDUserService service
	
	@Test
	void ok() {
		def details = service.loadUserByUsername("test.openid")
		assert details != null
		assert details.getPassword() == ""
		assert details.getUsername() == "Mr John"
		assert details.getAuthorities() == AuthorityUtils.createAuthorityList("ROLE_TEACHER")
		assert details.isEnabled()
	}
	
	@Test(expected = UsernameNotFoundException.class)
	void not_found() {
		service.loadUserByUsername("test.notfound")
	}

}
