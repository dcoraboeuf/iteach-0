package net.iteach.service.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import net.iteach.api.model.AuthenticationMode;
import net.iteach.service.db.SQL;
import net.iteach.service.impl.AbstractServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("openIDUserService")
public class OpenIDUserService extends AbstractServiceImpl implements UserDetailsService {

	@Autowired
	public OpenIDUserService(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		List<UserAccount> users = getNamedParameterJdbcTemplate().query(SQL.USER_BY_OPENID, params("identifier", username), new RowMapper<UserAccount>() {
			@Override
			public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserAccount(
						AuthenticationMode.openid,
						username,
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("lastName"));
			}
		});
		if (users.size() != 1) {
			throw new UsernameNotFoundException(username);
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
					return null;
				}
				
				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					// FIXME Implement Type1355086304191.getAuthorities
					return AuthorityUtils.createAuthorityList("ROLE_TEACHER");
				}
			};
		}
	}

}
