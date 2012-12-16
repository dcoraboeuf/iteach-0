package net.iteach.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import net.iteach.core.security.User;

public class UserDefinition implements User {
	
	private final UserAccount account;
	
	public UserDefinition(UserAccount account) {
		this.account = account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// FIXME Implement UserDetails.getAuthorities
		return AuthorityUtils.createAuthorityList("ROLE_TEACHER");
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return String.format("%s %s", account.getFirstName(), account.getLastName());
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
		// FIXME Implement UserDetails.isEnabled
		return true;
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}

	@Override
	public int getId() {
		return account.getId();
	}
	
	

}
