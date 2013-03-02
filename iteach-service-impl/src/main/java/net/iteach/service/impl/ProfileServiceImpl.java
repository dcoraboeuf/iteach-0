package net.iteach.service.impl;

import net.iteach.api.PreferenceService;
import net.iteach.api.ProfileService;
import net.iteach.api.SecurityService;
import net.iteach.core.model.*;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.db.SQL;
import net.iteach.service.db.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

@Service
public class ProfileServiceImpl extends AbstractServiceImpl implements
        ProfileService {

    private final SecurityUtils securityUtils;
    private final SecurityService securityService;
    private final PreferenceService preferenceService;

    @Autowired
    public ProfileServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils, SecurityService securityService, PreferenceService preferenceService) {
        super(dataSource, validator);
        this.securityUtils = securityUtils;
        this.securityService = securityService;
        this.preferenceService = preferenceService;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountProfile getProfile() {
        int userId = securityUtils.getCurrentUserId();
        return getProfile(userId);
    }

    @Override
    @Transactional
    public void savePreferences(Map<PreferenceKey, String> preferences) {
        for (Map.Entry<PreferenceKey, String> entry : preferences.entrySet()) {
            PreferenceKey key = entry.getKey();
            String value = entry.getValue();
            preferenceService.setPreference(key, value);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Secured(SecurityRoles.ADMINISTRATOR)
    public AccountProfile getProfile(final int userId) {
        // OK
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.USER_BY_ID,
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
                        // Gets all the preferences for the user
                        Preferences preferences = preferenceService.getPreferences();
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
                                lessonCount,
                                preferences);
                    }
                });
    }

    @Override
    public void passwordRequest(Locale locale) {
        int userId = securityUtils.getCurrentUserId();
        securityService.passwordRequest(locale, userId);
    }

    @Override
    public void passwordChangeCheck(String token) {
        int userId = securityUtils.getCurrentUserId();
        securityService.checkTokenForUserId(TokenType.PASSWORD_REQUEST, token, userId);
    }

    @Override
    public Ack passwordChange(String token, String oldPassword, String newPassword) {
        int userId = securityUtils.getCurrentUserId();
        return securityService.passwordChange(userId, token, oldPassword, newPassword);
    }

}
