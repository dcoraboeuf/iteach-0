package net.iteach.service.dao;

import net.iteach.service.dao.model.TLesson;
import org.joda.time.LocalDate;

import java.util.List;

public interface LessonDao {

    List<TLesson> findLessonsForStudent(int studentId, LocalDate from, LocalDate to);
}
