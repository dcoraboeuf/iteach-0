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

import net.iteach.api.admin.AccountDetails;
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
	
	public static class AccountSummaryRowMapper implements
			RowMapper<AccountSummary> {
		
		private final int userId;

		public AccountSummaryRowMapper(int userId) {
			this.userId = userId;
		}

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
	}

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
			new AccountSummaryRowMapper(userId));
	}
	
	@Override
	@Transactional(readOnly = true)
	@Secured(SecurityRoles.ADMINISTRATOR)
	public AccountDetails getAccountDetails(int id) {
		// Current user
		int userId = securityUtils.getCurrentUserId();
		// Summary
		AccountSummary summary = getNamedParameterJdbcTemplate().queryForObject(
			SQL.ADMIN_ACCOUNT_BY_ID,
			params("id", id),
			new AccountSummaryRowMapper(userId));
		// Count of schools
		int schoolCount = getNamedParameterJdbcTemplate().queryForList(
			SQL.SCHOOL_IDS_FOR_TEACHER,
			params("teacher", id),
			Integer.class).size();
		// Count of students
		int studentCount = getNamedParameterJdbcTemplate().queryForList(
				SQL.STUDENT_IDS_FOR_TEACHER,
				params("teacher", id),
				Integer.class).size();
		// Count of lessons
		int lessonCount = getNamedParameterJdbcTemplate().queryForList(
				SQL.LESSON_IDS_FOR_TEACHER,
				params("teacher", id),
				Integer.class).size();
		// OK
		return new AccountDetails(summary, schoolCount, studentCount, lessonCount);
	}

}
