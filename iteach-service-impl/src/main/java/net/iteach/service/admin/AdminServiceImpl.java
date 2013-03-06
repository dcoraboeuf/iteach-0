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
import net.iteach.api.model.copy.*;
import net.iteach.core.model.AccountProfile;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.CommentSummary;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.dao.*;
import net.iteach.service.dao.model.TLesson;
import net.iteach.service.dao.model.TSchool;
import net.iteach.service.dao.model.TStudent;
import net.iteach.service.dao.model.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final SecurityUtils securityUtils;
    private final ProfileService profileService;
    private final SchoolDao schoolDao;
    private final StudentDao studentDao;
    private final LessonDao lessonDao;
    private final CommentsService commentsService;
    private final CoordinatesService coordinatesService;
    private final ConfigurationDao configurationDao;
    private final UserDao userDao;

    @Autowired
    public AdminServiceImpl(SecurityUtils securityUtils, ProfileService profileService, SchoolDao schoolDao, StudentDao studentDao, LessonDao lessonDao, CommentsService commentsService, CoordinatesService coordinatesService, ConfigurationDao configurationDao, UserDao userDao) {
        this.securityUtils = securityUtils;
        this.profileService = profileService;
        this.schoolDao = schoolDao;
        this.studentDao = studentDao;
        this.lessonDao = lessonDao;
        this.commentsService = commentsService;
        this.coordinatesService = coordinatesService;
        this.configurationDao = configurationDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    @Secured(SecurityRoles.ADMINISTRATOR)
    public Settings getSettings() {
        List<Setting> list = Lists.transform(Arrays.asList(ConfigurationKey.values()),
                new Function<ConfigurationKey, Setting>() {
                    @Override
                    public Setting apply(ConfigurationKey key) {
                        String value = configurationDao.getValue(key.name());
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
            // Gets the input value
            String value = update.getValue(key);
            // TODO Control for the value
            // Updates the configuration value
            configurationDao.setValue(key.name(), value);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Secured(SecurityRoles.ADMINISTRATOR)
    public List<AccountSummary> getAccounts() {
        // Current user
        final int userId = securityUtils.getCurrentUserId();
        // All users
        return Lists.transform(
                userDao.findAll(),
                loadAccountSummaryFn(userId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    @Secured(SecurityRoles.ADMINISTRATOR)
    public AccountSummary getAccount(int id) {
        // Current user
        int userId = securityUtils.getCurrentUserId();
        // Loads the user data
        return loadAccountSummaryFn(userId).apply(userDao.getUserById(id));

    }

    private Function<TUser, AccountSummary> loadAccountSummaryFn(final int userId) {
        return new Function<TUser, AccountSummary>() {
            @Override
            public AccountSummary apply(TUser user) {
                // Gets the profile
                AccountProfile profile = profileService.getProfile(user.getId());
                // Summary
                return new AccountSummary(
                        user.getId() == userId,
                        user.getId(),
                        profile.getMode(),
                        profile.getFirstName(),
                        profile.getLastName(),
                        profile.getEmail(),
                        profile.isAdmin(),
                        user.isVerified(),
                        profile.getSchoolCount(),
                        profile.getStudentCount(),
                        profile.getLessonCount());
            }
        };
    }

    @Override
    @Transactional
    @Secured(SecurityRoles.ADMINISTRATOR)
    public void deleteAccount(int id) {
        userDao.deleteUser(id);
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

    private List<ExportedComment> exportedComments(final CommentEntity entity, final int id) {
        return Lists.transform(
                commentsService.getComments(entity, id, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, CommentFormat.RAW).getList(),
                new Function<CommentSummary, ExportedComment>() {
                    @Override
                    public ExportedComment apply(CommentSummary it) {
                        Comment c = commentsService.getComment(entity, id, it.getId(), CommentFormat.RAW);
                        return new ExportedComment(
                                c.getCreation(),
                                c.getEdition(),
                                c.getContent()
                        );
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

    @Override
    @Transactional
    public AccountSummary importData(int id, MultipartFile file) {
        // Gets the account
        AccountSummary account = getAccount(id);
        // FIXME Importing data
        // OK
        return account;
    }
}
