package net.iteach.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.ProfileService;
import net.iteach.core.model.AccountProfile;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl extends AbstractServiceImpl implements
		ProfileService {

	private final SecurityUtils securityUtils;

	@Autowired
	public ProfileServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils) {
		super(dataSource, validator);
		this.securityUtils = securityUtils;
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

}
