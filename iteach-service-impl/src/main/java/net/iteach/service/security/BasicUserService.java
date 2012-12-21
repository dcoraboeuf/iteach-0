package net.iteach.service.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.iteach.api.model.AuthenticationMode;
import net.iteach.service.db.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service("basicUserService")
public class BasicUserService extends AbstractUserService {

	@Autowired
	public BasicUserService(DataSource dataSource) {
		super(dataSource,SQL.USER_BY_PASSWORD);
	}
	
	@Override
	protected RowMapper<UserAccount> getAccountRowMapper(final String identifier) {
		return new RowMapper<UserAccount>() {
			@Override
			public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserAccount(
						rs.getInt("id"),
						AuthenticationMode.password,
						identifier,
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("lastName"));
			}
		};
	}

}
