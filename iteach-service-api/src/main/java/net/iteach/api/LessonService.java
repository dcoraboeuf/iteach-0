package net.iteach.api;

import org.joda.time.LocalDate;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.LessonForm;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.model.StudentLessons;

public interface LessonService {

	Lessons getLessonsForTeacher(int userId, LessonRange range);

	ID createLessonForTeacher(int userId, LessonForm form);

	Ack editLessonForTeacher(int userId, int id, LessonForm form);

	Ack deleteLessonForTeacher(int userId, int id);

	StudentLessons getLessonsForStudent(int userId, int id, LocalDate date);

}
