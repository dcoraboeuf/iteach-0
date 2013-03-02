package net.iteach.service.admin;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.iteach.api.CommentsService;
import net.iteach.api.CoordinatesService;
import net.iteach.api.ProfileService;
import net.iteach.api.admin.*;
import net.iteach.api.model.CommentEntity;
import net.iteach.api.model.ConfigurationKey;
import net.iteach.api.model.CoordinateEntity;
import net.iteach.api.model.copy.ExportedLesson;
import net.iteach.api.model.copy.ExportedSchool;
import net.iteach.api.model.copy.ExportedStudent;
import net.iteach.api.model.copy.ExportedTeacher;
import net.iteach.core.model.AccountProfile;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.CommentSummary;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.dao.LessonDao;
import net.iteach.service.dao.SchoolDao;
import net.iteach.service.dao.StudentDao;
import net.iteach.service.dao.model.TLesson;
import net.iteach.service.dao.model.TSchool;
import net.iteach.service.dao.model.TStudent;
import net.iteach.service.db.SQL;
import net.iteach.service.impl.AbstractServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
    private final SchoolDao schoolDao;
    private final StudentDao studentDao;
    private final LessonDao lessonDao;
    private final CommentsService commentsService;
    private final CoordinatesService coordinatesService;

    @Autowired
    public AdminServiceImpl(DataSource dataSource, Validator validator, SecurityUtils securityUtils, ProfileService profileService, SchoolDao schoolDao, StudentDao studentDao, LessonDao lessonDao, CommentsService commentsService, CoordinatesService coordinatesService) {
        super(dataSource, validator);
        this.securityUtils = securityUtils;
        this.profileService = profileService;
        this.schoolDao = schoolDao;
        this.studentDao = studentDao;
        this.lessonDao = lessonDao;
        this.commentsService = commentsService;
        this.coordinatesService = coordinatesService;
    }

    @Override
    @Transactional(readOnly = true)
    @Secured(SecurityRoles.ADMINISTRATOR)
    public Settings getSettings() {
        List<Setting> list = Lists.transform(Arrays.asList(ConfigurationKey.values()),
                new Function<ConfigurationKey, Setting>() {
                    @Override
                    public Setting apply(ConfigurationKey key) {
                        String value = getFirstItem(SQL.CONFIGURATION_GET, params("name", key.name()), String.class);
                        String defaultValue = key.getDefaultValue();
                        return new Setting(key, defaultValue, value, key.getType());
                    }
                });
        return new Settings(list);
    }

    @Override
    @Transactional
    @Secured(SecurityRoles.ADMINISTRATOR)
    public void setSettings(SettingsUpdate update) {
        for (ConfigurationKey key : ConfigurationKey.values()) {
            // Deletes the property in all cases
            getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_DELETE, params("name", key.name()));
            // Gets the input value
            String value = update.getValue(key);
            // Update if not blank
            if (StringUtils.isNotBlank(value)) {
                // TODO Control for the value
                // Updates the value
                getNamedParameterJdbcTemplate().update(SQL.CONFIGURATION_SET, params("name", key.name()).addValue("value", value));
            }
        }
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

    @Override
    @Transactional(readOnly = true)
    @Secured(SecurityRoles.ADMINISTRATOR)
    public ExportedTeacher export(int id) {
        return new ExportedTeacher(
                exportedSchools(id)
        );
    }

    private List<ExportedSchool> exportedSchools(int userId) {
        return Lists.transform(
                schoolDao.findSchoolsByTeacher(userId),
                new Function<TSchool, ExportedSchool>() {
                    @Override
                    public ExportedSchool apply(TSchool t) {
                        return new ExportedSchool(
                                exportedComments(CommentEntity.SCHOOL, t.getId()),
                                coordinatesService.getCoordinates(CoordinateEntity.SCHOOL, t.getId()).getList(),
                                t.getName(),
                                t.getColor(),
                                t.getHourlyRate(),
                                exportedStudents(t.getId())
                        );
                    }
                }
        );
    }

    private List<Comment> exportedComments(final CommentEntity entity, final int id) {
        return Lists.transform(
                commentsService.getComments(entity, id, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, CommentFormat.RAW).getList(),
                new Function<CommentSummary, Comment>() {
                    @Override
                    public Comment apply(CommentSummary it) {
                        return commentsService.getComment(entity, id, it.getId(), CommentFormat.RAW);
                    }
                }
        );
    }

    private List<ExportedStudent> exportedStudents(int schoolId) {
        return Lists.transform(
                studentDao.findStudentsBySchool(schoolId),
                new Function<TStudent, ExportedStudent>() {
                    @Override
                    public ExportedStudent apply(TStudent t) {
                        return new ExportedStudent(
                                exportedComments(CommentEntity.STUDENT, t.getId()),
                                coordinatesService.getCoordinates(CoordinateEntity.STUDENT, t.getId()).getList(),
                                t.getName(),
                                t.getSubject(),
                                t.isDisabled(),
                                exportedLessons(t.getId())
                        );
                    }
                }
        );
    }

    private List<ExportedLesson> exportedLessons(int studentId) {
        return Lists.transform(
                lessonDao.findAllLessonsForStudent(studentId),
                new Function<TLesson, ExportedLesson>() {
                    @Override
                    public ExportedLesson apply(TLesson t) {
                        return new ExportedLesson(
                                exportedComments(CommentEntity.LESSON, t.getId()),
                                t.getDate(),
                                t.getFrom(),
                                t.getTo(),
                                t.getLocation()
                        );
                    }
                }
        );
    }

}
