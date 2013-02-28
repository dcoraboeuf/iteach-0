package net.iteach.web.ui;

import net.iteach.api.ProfileService;
import net.iteach.api.TeacherService;
import net.iteach.core.model.*;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.AbstractUIController;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping("/ui/teacher")
public class TeacherUIController extends AbstractUIController implements TeacherUI {

    private final ProfileService profileService;
    private final TeacherService teacherService;

    @Autowired
    public TeacherUIController(ProfileService profileService,
                               TeacherService teacherService,
                               SecurityUtils securityUtils,
                               ErrorHandler errorHandler,
                               Strings strings) {
        super(securityUtils, errorHandler, strings);
        this.profileService = profileService;
        this.teacherService = teacherService;
    }

    @Override
    @RequestMapping(value = "/school", method = RequestMethod.GET)
    public
    @ResponseBody
    SchoolSummaries getSchools() {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getSchoolsForTeacher(userId);
    }

    @Override
    @RequestMapping(value = "/school/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SchoolDetails getSchool(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getSchoolForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/school", method = RequestMethod.POST)
    public
    @ResponseBody
    ID createSchool(@RequestBody SchoolForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.createSchoolForTeacher(userId, form);
    }

    @Override
    @RequestMapping(value = "/school/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Ack deleteSchool(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.deleteSchoolForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/school/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Ack editSchool(@PathVariable int id, @RequestBody SchoolForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.editSchoolForTeacher(userId, id, form);
    }

    @Override
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public
    @ResponseBody
    StudentSummaries getStudents() {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getStudentsForTeacher(userId);
    }

    @Override
    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public
    @ResponseBody
    ID createStudent(@RequestBody StudentForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.createStudentForTeacher(userId, form);
    }

    @Override
    @RequestMapping(value = "/student/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Ack deleteStudent(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.deleteStudentForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/student/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Ack editStudent(@PathVariable int id, @RequestBody StudentForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.editStudentForTeacher(userId, id, form);
    }

    @Override
    @RequestMapping(value = "/student/{id}/disable", method = RequestMethod.PUT)
    public
    @ResponseBody
    Ack disableStudent(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.disableStudentForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/student/{id}/enable", method = RequestMethod.PUT)
    public
    @ResponseBody
    Ack enableStudent(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.enableStudentForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    StudentDetails getStudent(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getStudentForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/student/{id}/lessons/{date}", method = RequestMethod.GET)
    public
    @ResponseBody
    StudentLessons getStudentLessons(@PathVariable int id, @PathVariable LocalDate date, Locale locale) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getLessonsForStudent(userId, id, date, locale);
    }

    @Override
    @RequestMapping(value = "/lesson", method = RequestMethod.GET)
    public
    @ResponseBody
    Lessons getLessons(@RequestBody LessonRange range) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getLessonsForTeacher(userId, range);
    }

    @Override
    @RequestMapping(value = "/lesson/{id:\\d+}", method = RequestMethod.GET)
    public
    @ResponseBody
    LessonDetails getLesson(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getLessonDetails(userId, id);
    }

    @Override
    @RequestMapping(value = "/lesson", method = RequestMethod.POST)
    public
    @ResponseBody
    ID createLesson(@RequestBody LessonForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.createLessonForTeacher(userId, form);
    }

    @Override
    @RequestMapping(value = "/lesson/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Ack editLesson(@PathVariable int id, @RequestBody LessonForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.editLessonForTeacher(userId, id, form);
    }

    @Override
    @RequestMapping(value = "/lesson/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Ack deleteLesson(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.deleteLessonForTeacher(userId, id);
    }

    @Override
    @RequestMapping(value = "/lesson/{id}/change", method = RequestMethod.POST)
    public
    @ResponseBody
    Ack changeLesson(@PathVariable int id, @RequestBody LessonChange change) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.changeLessonForTeacher(userId, id, change);
    }

    @Override
    @RequestMapping(value = "/student/{id:\\d+}/coordinates", method = RequestMethod.GET)
    public
    @ResponseBody
    Coordinates getStudentCoordinates(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getStudentCoordinates(userId, id);
    }

    @Override
    @RequestMapping(value = "/school/{id:\\d+}/coordinates", method = RequestMethod.GET)
    public
    @ResponseBody
    Coordinates getSchoolCoordinates(@PathVariable int id) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getSchoolCoordinates(userId, id);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/list/{maxlength:\\d+}/{format}/{offset:\\d+}/{count:\\d+}", method = RequestMethod.GET)
    public
    @ResponseBody
    Comments getLessonComments(@PathVariable int lessonId, @PathVariable int offset, @PathVariable int count, @PathVariable int maxlength, @PathVariable CommentFormat format) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getLessonComments(userId, lessonId, offset, count, maxlength, format);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/{commentId:\\d+}/{format:.*}", method = RequestMethod.GET)
    public
    @ResponseBody
    Comment getLessonComment(@PathVariable int lessonId, @PathVariable int commentId, @PathVariable CommentFormat format) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getLessonComment(userId, lessonId, commentId, format);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/{format:.*}", method = RequestMethod.POST)
    public
    @ResponseBody
    Comment editLessonComment(@PathVariable int lessonId, @PathVariable CommentFormat format, @RequestBody CommentsForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.editLessonComment(userId, lessonId, format, form);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId:\\d+}/comment/{commentId:\\d+}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Ack deleteLessonComment(@PathVariable int lessonId, @PathVariable int commentId) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.deleteLessonComment(userId, lessonId, commentId);
    }

    @Override
    @RequestMapping(value = "/school/{schoolId:\\d+}/comment/list/{maxlength:\\d+}/{format}/{offset:\\d+}/{count:\\d+}", method = RequestMethod.GET)
    public
    @ResponseBody
    Comments getSchoolComments(@PathVariable int schoolId, @PathVariable int offset, @PathVariable int count, @PathVariable int maxlength, @PathVariable CommentFormat format) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getSchoolComments(userId, schoolId, offset, count, maxlength, format);
    }

    @Override
    @RequestMapping(value = "/school/{schoolId:\\d+}/comment/{commentId:\\d+}/{format:.*}", method = RequestMethod.GET)
    public
    @ResponseBody
    Comment getSchoolComment(@PathVariable int schoolId, @PathVariable int commentId, @PathVariable CommentFormat format) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getSchoolComment(userId, schoolId, commentId, format);
    }

    @Override
    @RequestMapping(value = "/school/{schoolId:\\d+}/comment/{format:.*}", method = RequestMethod.POST)
    public
    @ResponseBody
    Comment editSchoolComment(@PathVariable int schoolId, @PathVariable CommentFormat format, @RequestBody CommentsForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.editSchoolComment(userId, schoolId, format, form);
    }

    @Override
    @RequestMapping(value = "/school/{schoolId:\\d+}/comment/{commentId:\\d+}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Ack deleteSchoolComment(@PathVariable int schoolId, @PathVariable int commentId) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.deleteSchoolComment(userId, schoolId, commentId);
    }

    @Override
    @RequestMapping(value = "/student/{studentId:\\d+}/comment/list/{maxlength:\\d+}/{format}/{offset:\\d+}/{count:\\d+}", method = RequestMethod.GET)
    public
    @ResponseBody
    Comments getStudentComments(@PathVariable int studentId, @PathVariable int offset, @PathVariable int count, @PathVariable int maxlength, @PathVariable CommentFormat format) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getStudentComments(userId, studentId, offset, count, maxlength, format);
    }

    @Override
    @RequestMapping(value = "/student/{studentId:\\d+}/comment/{commentId:\\d+}/{format:.*}", method = RequestMethod.GET)
    public
    @ResponseBody
    Comment getStudentComment(@PathVariable int studentId, @PathVariable int commentId, @PathVariable CommentFormat format) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.getStudentComment(userId, studentId, commentId, format);
    }

    @Override
    @RequestMapping(value = "/student/{studentId:\\d+}/comment/{format:.*}", method = RequestMethod.POST)
    public
    @ResponseBody
    Comment editStudentComment(@PathVariable int studentId, @PathVariable CommentFormat format, @RequestBody CommentsForm form) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.editStudentComment(userId, studentId, format, form);
    }

    @Override
    @RequestMapping(value = "/student/{studentId:\\d+}/comment/{commentId:\\d+}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Ack deleteStudentComment(@PathVariable int studentId, @PathVariable int commentId) {
        // Gets the current teacher
        int userId = securityUtils.getCurrentUserId();
        // OK
        return teacherService.deleteStudentComment(userId, studentId, commentId);
    }

    @Override
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public
    @ResponseBody
    AccountProfile getProfile() {
        return profileService.getProfile();
    }

}
