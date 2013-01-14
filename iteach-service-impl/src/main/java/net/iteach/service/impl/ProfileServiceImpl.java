package net.iteach.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.ProfileService;
import net.iteach.api.SecurityService;
import net.iteach.core.model.AccountProfile;
import net.iteach.core.model.AuthenticationMode;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl extends AbstractServiceImpl implements
		ProfileService {

	private final SecurityUtils securityUtils;
	private final SecurityService securityService;

	@Autowired
	public ProfileServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils, SecurityService securityService) {
		super(dataSource, validator);
		this.securityUtils = securityUtils;
		this.securityService = securityService;
	}

	@Override
	@Transactional(readOnly = true)
	public AccountProfile getProfile() {
		int userId = securityUtils.getCurrentUserId();
		return getProfile(userId);
	}

	@Override
	@Transactional(readOnly = true)
	@Secured(SecurityRoles.ADMINISTRATOR)
	public AccountProfile getProfile(final int userId) {
		// OK
		return getNamedParameterJdbcTemplate().queryForObject(
			SQL.PROFILE,
			params("id", userId),
			new RowMapper<AccountProfile>() {
				@Override
					public AccountProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
					// Count of schools
					int schoolCount = getNamedParameterJdbcTemplate().queryForList(
						SQL.SCHOOL_IDS_FOR_TEACHER,
						params("teacher", userId),
						Integer.class).size();
					// Count of students
					int studentCount = getNamedParameterJdbcTemplate().queryForList(
							SQL.STUDENT_IDS_FOR_TEACHER,
							params("teacher", userId),
							Integer.class).size();
					// Count of lessons
					int lessonCount = getNamedParameterJdbcTemplate().queryForList(
							SQL.LESSON_IDS_FOR_TEACHER,
							params("teacher", userId),
							Integer.class).size();
					// OK
					return new AccountProfile(
							userId,
							SQLUtils.getEnum(AuthenticationMode.class, rs, "mode"),
							rs.getString("firstName"),
							rs.getString("lastName"),
							rs.getString("email"),
							rs.getBoolean("administrator"),
							schoolCount,
							studentCount,
							lessonCount);
				}
			});
	}
	
	@Override
	public void passwordRequest(Locale locale) {
		int userId = securityUtils.getCurrentUserId();
		securityService.passwordRequest(locale, userId);
	}

}
