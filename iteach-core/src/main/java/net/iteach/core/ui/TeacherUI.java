package net.iteach.core.ui;

import net.iteach.core.model.Ack;
import net.iteach.core.model.Comment;
import net.iteach.core.model.CommentFormat;
import net.iteach.core.model.Comments;
import net.iteach.core.model.CommentsForm;
import net.iteach.core.model.Coordinates;
import net.iteach.core.model.ID;
import net.iteach.core.model.LessonDetails;
import net.iteach.core.model.LessonForm;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.model.SchoolDetails;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.model.StudentDetails;
import net.iteach.core.model.StudentForm;
import net.iteach.core.model.StudentLessons;
import net.iteach.core.model.StudentSummaries;

import org.joda.time.LocalDate;

public interface TeacherUI {

	SchoolSummaries getSchools();

	ID createSchool(SchoolForm form);

	Ack deleteSchool(int id);

	Ack editSchool(int id, SchoolForm form);

	StudentSummaries getStudents();

	ID createStudent(StudentForm form);

	Ack deleteStudent(int id);

	Ack editStudent(int id, StudentForm form);

	ID createLesson(LessonForm form);

	Lessons getLessons(LessonRange range);

	Ack editLesson(int id, LessonForm form);

	Ack deleteLesson(int id);
	
	StudentDetails getStudent (int id);

	StudentLessons getStudentLessons(int id, LocalDate date);

	SchoolDetails getSchool(int id);

	LessonDetails getLesson(int id);

	Coordinates getStudentCoordinates(int id);

	Coordinates getSchoolCoordinates(int id);
	
	// Comments
	
	Comments getStudentComments (int studentId, int offset, int count, CommentFormat format);
	
	Comment getStudentComment(int studentId, int commentId, CommentFormat format);

	Comment editStudentComment(int studentId, CommentFormat format, CommentsForm form);

}
