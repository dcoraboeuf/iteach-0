package net.iteach.service.security;

import java.util.Collection;

import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class UserDefinition implements User {
	
	private final UserAccount account;
	
	public UserDefinition(UserAccount account) {
		this.account = account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (account.isAdministrator()) {
			return AuthorityUtils.createAuthorityList(SecurityRoles.TEACHER, SecurityRoles.ADMINISTRATOR);
		} else {
			return AuthorityUtils.createAuthorityList(SecurityRoles.TEACHER);
		}
	}
	
	@Override
	public boolean isAdmin() {
		return account.isAdministrator();
	}
	
	@Override
	public String getEmail() {
		return account.getEmail();
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
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !account.isDisabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
        return account.isVerified() && !account.isDisabled();
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
