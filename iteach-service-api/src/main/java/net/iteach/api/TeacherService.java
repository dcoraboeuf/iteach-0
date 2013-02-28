package net.iteach.api;

import net.iteach.core.model.*;

import java.math.BigDecimal;

public interface TeacherService extends LessonService {

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

}
