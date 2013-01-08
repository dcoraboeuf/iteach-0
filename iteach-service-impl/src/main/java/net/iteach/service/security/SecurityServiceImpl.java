package net.iteach.service.security;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.SecurityService;
import net.iteach.api.model.AuthenticationMode;
import net.iteach.service.db.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityServiceImpl extends AbstractSecurityService implements SecurityService {
	
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, PasswordEncoder passwordEncoder) {
		super(dataSource, validator);
		this.passwordEncoder = passwordEncoder;
	}

	protected String digest(String password, String email) {
		return passwordEncoder.encodePassword(password, email);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isAdminInitialized() {
		return getJdbcTemplate().queryForInt(SQL.USER_ADMINISTRATOR_COUNT) > 0;
	}

	@Override
	@Transactional
	public void register(AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password) {
		// FIXME Controls
		// Checks for unicity of identifier
		Integer existingUserId = getFirstItem(SQL.USER_BY_IDENTIFIER, params("identifier", identifier), Integer.class);
		if (existingUserId != null) {
			throw new UserIdentifierAlreadyExistsException(identifier);
		}
		// Checks for unicity of email
		existingUserId = getFirstItem(SQL.USER_BY_EMAIL, params("email", email), Integer.class);
		if (existingUserId != null) {
			throw new UserEmailAlreadyExistsException(email);
		}
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
			params.addValue("password", digest(password, email));
		} else {
			throw new UnknownAuthenticationModeException(mode);
		}
		// Insert the user
		getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, params);
	}

}
