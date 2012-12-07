package net.iteach.service.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.iteach.api.SecurityService;
import net.iteach.api.model.AuthenticationMode;
import net.iteach.service.db.SQL;
import net.iteach.service.impl.AbstractServiceImpl;

@Service
public class SecurityServiceImpl extends AbstractServiceImpl implements SecurityService {

	protected static String digest(String input) {
		return Sha512DigestUtils.shaHex(input);
	}

	@Autowired
	public SecurityServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	@Transactional
	public void register(AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password) {
		// FIXME Controls
		// Parameters
		MapSqlParameterSource params = params("email", email);
		params.addValue("firstName", firstName);
		params.addValue("lastName", lastName);
		// Mode
		params.addValue("mode", mode.name());
		if (mode == AuthenticationMode.openid) {
			params.addValue("identifier", identifier);
			params.addValue("password", "");
		} else if (mode == AuthenticationMode.password) {
			params.addValue("identifier", email);
			params.addValue("password", digest(password));
		} else {
			throw new UnknownAuthenticationModeException(mode);
		}
		// Insert the user
		getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, params);
	}

}
