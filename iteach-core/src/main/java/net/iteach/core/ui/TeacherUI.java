package net.iteach.core.ui;

import net.iteach.core.model.*;
import org.joda.time.LocalDate;

import java.util.Locale;

public interface TeacherUI {

	SchoolSummaries getSchools();

	ID createSchool(SchoolForm form);

	Ack deleteSchool(int id);

	Ack editSchool(int id, SchoolForm form);

	StudentSummaries getStudents();

	ID createStudent(StudentForm form);

	Ack deleteStudent(int id);

	Ack editStudent(int id, StudentForm form);

    Ack disableStudent(int id);

    Ack enableStudent(int id);

	ID createLesson(LessonForm form);

	Lessons getLessons(LessonRange range);

	Ack editLesson(int id, LessonForm form);

	Ack deleteLesson(int id);
	
	StudentDetails getStudent (int id);

	StudentLessons getStudentLessons(int id, LocalDate date, Locale locale);

	SchoolDetails getSchool(int id);

	LessonDetails getLesson(int id);

	Coordinates getStudentCoordinates(int id);

	Coordinates getSchoolCoordinates(int id);

    Ack changeLesson (int id, LessonChange change);

    Ack moveLesson (int id, LessonChange change);
	
	// Comments for lessons
	
	Comments getLessonComments (int lessonId, int offset, int count, int maxlength, CommentFormat format);
	
	Comment getLessonComment(int lessonId, int commentId, CommentFormat format);

	Comment editLessonComment(int lessonId, CommentFormat format, CommentsForm form);

	Ack deleteLessonComment(int lessonId, int commentId);
	
	// Comments for students
	
	Comments getStudentComments (int studentId, int offset, int count, int maxlength, CommentFormat format);
	
	Comment getStudentComment(int studentId, int commentId, CommentFormat format);

	Comment editStudentComment(int studentId, CommentFormat format, CommentsForm form);

	Ack deleteStudentComment(int studentId, int commentId);
	
	// Comments for schools
	
	Comments getSchoolComments (int schoolId, int offset, int count, int maxlength, CommentFormat format);
	
	Comment getSchoolComment(int schoolId, int commentId, CommentFormat format);

	Comment editSchoolComment(int schoolId, CommentFormat format, CommentsForm form);

	Ack deleteSchoolComment(int schoolId, int commentId);
	
	// Profile management
	
	AccountProfile getProfile();
}
