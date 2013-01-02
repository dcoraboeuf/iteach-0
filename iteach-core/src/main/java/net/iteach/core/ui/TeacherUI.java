package net.iteach.core.ui;

import org.joda.time.LocalDate;

import net.iteach.core.model.Ack;
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

}
