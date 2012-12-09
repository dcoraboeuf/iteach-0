package net.iteach.service.security;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractUserService extends AbstractSecurityService implements UserDetailsService {
	
	private final String query;

	@Autowired
	public AbstractUserService(DataSource dataSource, String query) {
		super(dataSource);
		this.query = query;
	}

	@Override
	public UserDetails loadUserByUsername(final String identifier) throws UsernameNotFoundException {
		RowMapper<UserAccount> accountMapper = getAccountRowMapper(identifier);
		List<UserAccount> users = getNamedParameterJdbcTemplate().query(query, params("identifier", identifier), accountMapper);
		if (users.size() != 1) {
			throw new UsernameNotFoundException(identifier);
		} else {
			final UserAccount account = users.get(0);
			return new UserDetails() {
				
				@Override
				public boolean isEnabled() {
					// FIXME Implement Type1355086304191.isEnabled
					return true;
				}
				
				@Override
				public boolean isCredentialsNonExpired() {
					return false;
				}
				
				@Override
				public boolean isAccountNonLocked() {
					return false;
				}
				
				@Override
				public boolean isAccountNonExpired() {
					return false;
				}
				
				@Override
				public String getUsername() {
					return String.format("%s %s", account.getFirstName(), account.getLastName());
				}
				
				@Override
				public String getPassword() {
					return account.getPassword();
				}
				
				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					// FIXME Implement Type1355086304191.getAuthorities
					return AuthorityUtils.createAuthorityList("ROLE_TEACHER");
				}
			};
		}
	}

	protected abstract RowMapper<UserAccount> getAccountRowMapper(String identifier);

}
