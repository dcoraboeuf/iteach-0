package net.iteach.service.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.iteach.api.admin.AccountSummary;
import net.iteach.api.admin.AdminService;
import net.iteach.api.model.AuthenticationMode;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import net.iteach.service.impl.AbstractServiceImpl;

@Service
public class AdminServiceImpl extends AbstractServiceImpl implements AdminService {
	
	private final SecurityUtils securityUtils;

	@Autowired
	public AdminServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils) {
		super(dataSource, validator);
		this.securityUtils = securityUtils;
	}

	@Override
	@Transactional(readOnly = true)
	@Secured(SecurityRoles.ADMINISTRATOR)
	public List<AccountSummary> getAccounts() {
		// Current user
		final int userId = securityUtils.getCurrentUserId();
		// Query
		return getJdbcTemplate().query(
			SQL.ADMIN_ACCOUNTS,
			new RowMapper<AccountSummary>() {

				@Override
				public AccountSummary mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					int id = rs.getInt("id");
					return new AccountSummary(
						id == userId,
						id,
						SQLUtils.getEnum(AuthenticationMode.class, rs, "mode"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("email"),
						rs.getBoolean("administrator"),
						rs.getBoolean("verified"));
				}
				
			});
	}

}
