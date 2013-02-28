package net.iteach.service.dao;

import net.iteach.service.dao.model.TLesson;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;

public interface LessonDao {

    List<TLesson> findLessonsForStudent(int studentId, LocalDate from, LocalDate to);

    List<TLesson> findLessonsForTeacher(int userId, LocalDateTime from, LocalDateTime to);
}
