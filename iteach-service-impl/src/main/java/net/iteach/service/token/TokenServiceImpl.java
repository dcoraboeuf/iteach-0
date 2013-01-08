package net.iteach.service.token;

import java.sql.Timestamp;
import java.util.UUID;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.core.model.TokenType;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import net.iteach.service.impl.AbstractServiceImpl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl extends AbstractServiceImpl implements TokenService {

	private static final int RETENTION_DAYS = 30;
	private static final int EXPIRATION_DELAY = 15;

	@Autowired
	public TokenServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional
	public String generateToken(TokenType type, String key) {
		// Generates the token
		String token = createToken(type, key);
		// Creation date
		Timestamp creation = SQLUtils.toTimestamp(SQLUtils.now());
		// Saves the token
		getNamedParameterJdbcTemplate().update(
				SQL.TOKEN_SAVE,
				new MapSqlParameterSource()
					.addValue("token", token)
					.addValue("tokentype", type.name())
					.addValue("tokenkey", key)
					.addValue("creation", creation));
		// OK
		return token;
	}
	
	@Override
	@Transactional(readOnly = true)
	public void checkToken(String token, TokenType type, String key) {
			try {
				Timestamp creation = getNamedParameterJdbcTemplate().queryForObject(
						SQL.TOKEN_CHECK, 
						new MapSqlParameterSource()
							.addValue("token", token)
							.addValue("tokentype", type.name())
							.addValue("tokenkey", key),
						Timestamp.class);
				DateTime utcCreation = SQLUtils.getDateTime(creation);
				DateTime utcNow = DateTime.now(DateTimeZone.UTC);
				Days days = Days.daysBetween(utcCreation, utcNow);
				if (days.isGreaterThan(Days.days(EXPIRATION_DELAY))) {
					throw new TokenExpiredException (token, type, key);
				}
			} catch (EmptyResultDataAccessException ex) {
				throw new TokenNotFoundException (token, type, key);
			}
	}
	
	@Override
	@Transactional
	public void consumesToken(String token, TokenType type, String key) {
		// Checks the token
		checkToken(token, type, key);
		// Deletes the token
		getNamedParameterJdbcTemplate().update(
				SQL.TOKEN_DELETE,
				new MapSqlParameterSource()
					.addValue("tokentype", type.name())
					.addValue("tokenkey", key));
	}
	
	@Override
	@Transactional
	public int cleanup() {
		// Date cut-off
		Timestamp cutOff = SQLUtils.toTimestamp(DateTime.now(DateTimeZone.UTC).minusDays(RETENTION_DAYS));
		// SQL
		int count = getNamedParameterJdbcTemplate().update(SQL.TOKEN_CLEANUP, new MapSqlParameterSource("creation", cutOff));
		// OK
		return count;
		
	}

	private String createToken(TokenType type, String key) {
		String s = String.format("%s-%s-%s", UUID.randomUUID(), type, key);
		return Sha512DigestUtils.shaHex(s);
	}

}
