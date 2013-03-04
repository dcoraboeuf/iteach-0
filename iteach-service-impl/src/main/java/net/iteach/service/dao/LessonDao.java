package net.iteach.service.dao;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.LessonRange;
import net.iteach.service.dao.model.TLesson;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.util.List;

public interface LessonDao {

    List<TLesson> findAllLessonsForStudent(int studentId);

    List<TLesson> findLessonsForStudent(int studentId, LocalDate from, LocalDate to);

    List<TLesson> findLessonsForTeacher(int userId, LocalDateTime from, LocalDateTime to);

    BigDecimal getHoursForStudent(int studentId);

    TLesson getLessonById(int id);

    ID createLesson(int student, String location, LocalDate date, LocalTime from, LocalTime to);

    Ack updateLesson(int id, int student, String location, LocalDate date, LocalTime from, LocalTime to);

    Ack deleteLesson(int id);

    LessonRange getLessonRange(int lessonId);

    Ack setLessonRange(int lessonId, LocalDate pdate, LocalTime pfrom, LocalTime pto);

    List<TLesson> findAllLessonsForTeacher(int userId);
}
