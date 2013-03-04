package net.iteach.service.security

import net.iteach.test.AbstractIntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class OpenIDUserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private UserDetailsService openIDUserService

    @Test
    void ok() {
        def details = openIDUserService.loadUserByUsername("test.openid")
        assert details != null
        assert details.getPassword() == ""
        assert details.getUsername() == "Mr John"
        assert details.getAuthorities() == AuthorityUtils.createAuthorityList("ROLE_TEACHER")
        assert details.isEnabled()
    }

    @Test(expected = UsernameNotFoundException.class)
    void not_found() {
        openIDUserService.loadUserByUsername("test.notfound")
    }

}
