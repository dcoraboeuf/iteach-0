package net.iteach.service.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.ProfileService;
import net.iteach.api.admin.AccountSummary;
import net.iteach.api.admin.AdminService;
import net.iteach.core.model.AccountProfile;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;
import net.iteach.service.impl.AbstractServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl extends AbstractServiceImpl implements AdminService {
	
	public class AccountSummaryRowMapper implements RowMapper<AccountSummary> {
		
		private final int userId;

		public AccountSummaryRowMapper(int userId) {
			this.userId = userId;
		}

		@Override
		public AccountSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			// Gets the profile
			AccountProfile profile = profileService.getProfile(id);
			// Summary
			return new AccountSummary(
				id == userId,
				id,
				profile.getMode(),
				profile.getFirstName(),
				profile.getLastName(),
				profile.getEmail(),
				profile.isAdmin(),
				rs.getBoolean("verified"),
				profile.getSchoolCount(),
				profile.getStudentCount(),
				profile.getLessonCount());
		}
	}

	private final SecurityUtils securityUtils;
	private final ProfileService profileService;

	@Autowired
	public AdminServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils, ProfileService profileService) {
		super(dataSource, validator);
		this.securityUtils = securityUtils;
		this.profileService = profileService;
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
			new AccountSummaryRowMapper(userId));
	}
	
	@Override
	@Transactional(readOnly = true)
	@Secured(SecurityRoles.ADMINISTRATOR)
	public AccountSummary getAccount(int id) {
		// Current user
		int userId = securityUtils.getCurrentUserId();
		// Summary
		return getNamedParameterJdbcTemplate().queryForObject(
			SQL.ADMIN_ACCOUNT_BY_ID,
			params("id", id),
			new AccountSummaryRowMapper(userId));
	}
	
	@Override
	@Transactional
	@Secured(SecurityRoles.ADMINISTRATOR)
	public void deleteAccount(int id) {
		getNamedParameterJdbcTemplate().update(SQL.ADMIN_ACCOUNT_DELETE, params("id", id));
	}
	
}
