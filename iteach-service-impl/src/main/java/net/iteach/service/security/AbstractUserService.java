package net.iteach.service.security;

import java.util.List;

import javax.sql.DataSource;

import net.iteach.core.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
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
	public User loadUserByUsername(final String identifier) throws UsernameNotFoundException {
		RowMapper<UserAccount> accountMapper = getAccountRowMapper(identifier);
		List<UserAccount> users = getNamedParameterJdbcTemplate().query(query, params("identifier", identifier), accountMapper);
		if (users.size() != 1) {
			throw new UsernameNotFoundException(identifier);
		} else {
			final UserAccount account = users.get(0);
			return new UserDefinition(account);
		}
	}

	protected abstract RowMapper<UserAccount> getAccountRowMapper(String identifier);

}
