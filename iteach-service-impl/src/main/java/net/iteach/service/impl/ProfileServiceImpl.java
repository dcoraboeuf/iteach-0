package net.iteach.service.impl;

import net.iteach.api.PreferenceService;
import net.iteach.api.ProfileService;
import net.iteach.api.SecurityService;
import net.iteach.core.model.*;
import net.iteach.core.security.SecurityRoles;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.dao.LessonDao;
import net.iteach.service.dao.SchoolDao;
import net.iteach.service.dao.StudentDao;
import net.iteach.service.dao.UserDao;
import net.iteach.service.dao.model.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final SecurityUtils securityUtils;
    private final SecurityService securityService;
    private final PreferenceService preferenceService;
    private final UserDao userDao;
    private final SchoolDao schoolDao;
    private final StudentDao studentDao;
    private final LessonDao lessonDao;

    @Autowired
    public ProfileServiceImpl(SecurityUtils securityUtils, SecurityService securityService, PreferenceService preferenceService, UserDao userDao, SchoolDao schoolDao, StudentDao studentDao, LessonDao lessonDao) {
        this.securityUtils = securityUtils;
        this.securityService = securityService;
        this.preferenceService = preferenceService;
        this.userDao = userDao;
        this.schoolDao = schoolDao;
        this.studentDao = studentDao;
        this.lessonDao = lessonDao;
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
        // Loads the user
        TUser user = userDao.getUserById(userId);
        // Counts
        int schoolCount = schoolDao.findSchoolsByTeacher(userId).size();
        int studentCount = studentDao.findStudentsByTeacher(userId).size();
        int lessonCount = lessonDao.findAllLessonsForTeacher(userId).size();
        // Gets all the preferences for the user
        Preferences preferences = preferenceService.getPreferences();
        // OK
        return new AccountProfile(
                user.getId(),
                user.getMode(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isAdministrator(),
                schoolCount,
                studentCount,
                lessonCount,
                preferences
        );
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
