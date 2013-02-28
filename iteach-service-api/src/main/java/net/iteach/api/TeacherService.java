package net.iteach.api;

import net.iteach.core.model.*;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Locale;

public interface TeacherService {

	SchoolSummaries getSchoolsForTeacher(int teacherId);

	ID createSchoolForTeacher(int teacherId, SchoolForm form);

	Ack deleteSchoolForTeacher(int teacherId, int id);

	Ack editSchoolForTeacher(int userId, int id, SchoolForm form);

	SchoolDetails getSchoolForTeacher(int userId, int id);

	Coordinates getSchoolCoordinates(int userId, int id);

	Comments getSchoolComments(int userId, int schoolId, int offset, int count, int maxlength, CommentFormat format);

	Comment editSchoolComment(int userId, int schoolId, CommentFormat format, CommentsForm form);

	Comment getSchoolComment(int userId, int schoolId, int commentId, CommentFormat format);

	Ack deleteSchoolComment(int userId, int schoolId, int commentId);

    StudentSummaries getStudentsForTeacher(int teacherId);

    ID createStudentForTeacher(int teacherId, StudentForm form);

    Ack deleteStudentForTeacher(int teacherId, int id);

    Ack editStudentForTeacher(int userId, int id, StudentForm form);

    StudentDetails getStudentForTeacher(int userId, int id);

    BigDecimal getStudentHours(int userId, int id);

    Coordinates getStudentCoordinates(int userId, int id);

    Comments getStudentComments(int userId, int studentId, int offset, int count, int maxlength, CommentFormat format);

    Comment editStudentComment(int userId, int studentId, CommentFormat format, CommentsForm form);

    Comment getStudentComment(int userId, int studentId, int commentId, CommentFormat format);

    Ack deleteStudentComment(int userId, int studentId, int commentId);

    Ack disableStudentForTeacher(int userId, int id);

    Ack enableStudentForTeacher(int userId, int id);

    Lessons getLessonsForTeacher(int userId, LessonRange range);

    ID createLessonForTeacher(int userId, LessonForm form);

    Ack editLessonForTeacher(int userId, int id, LessonForm form);

    Ack deleteLessonForTeacher(int userId, int id);

    StudentLessons getLessonsForStudent(int userId, int id, LocalDate date, Locale locale);

    LessonDetails getLessonDetails(int userId, int id);

    Comments getLessonComments(int userId, int lessonId, int offset, int count, int maxlength, CommentFormat format);

    Comment editLessonComment(int userId, int lessonId, CommentFormat format, CommentsForm form);

    Comment getLessonComment(int userId, int lessonId, int commentId, CommentFormat format);

    Ack deleteLessonComment(int userId, int lessonId, int commentId);

    Ack changeLessonForTeacher(int userId, int id, LessonChange change);

}
