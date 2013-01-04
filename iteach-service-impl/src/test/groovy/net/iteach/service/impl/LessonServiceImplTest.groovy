package net.iteach.service.impl

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import net.iteach.api.LessonService
import net.iteach.core.model.Lesson
import net.iteach.core.model.LessonRange
import net.iteach.core.model.SchoolSummary
import net.iteach.core.model.StudentLesson
import net.iteach.core.model.StudentSummary
import net.iteach.test.AbstractIntegrationTest

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException

class LessonServiceImplTest extends AbstractIntegrationTest {
	
	@Autowired
	private LessonService service
	
	@Test(expected = AccessDeniedException)
	void getLessonsForStudent_access_denied() {
		service.getLessonsForStudent(2, 1, null)
	}
	
	@Test
	void getLessonsForStudent() {
		def testDate = new LocalDate(2013, 1, 4)
		def studentLessons = service.getLessonsForStudent(1, 1, testDate)
		assert studentLessons != null
		assert testDate == studentLessons.date
		assert 5 == studentLessons.hours
		def lessons = studentLessons.lessons
		assert lessons != null
		assert [
			new StudentLesson(2, new LocalDate(2013, 1, 7), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new StudentLesson(3, new LocalDate(2013, 1, 9), new LocalTime(18,0), new LocalTime(20,30), "Home")
			] == lessons		
	}
	
	@Test
	void getLessonsForTeacher_before() {
		def result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2012, 11, 1, 0, 0, 0), new LocalDateTime(2012, 11, 30, 23, 59, 59)))
		assert result != null
		assert result.lessons != null
		assert result.lessons.empty
	}
	
	@Test
	void getLessonsForTeacher_after() {
		def result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2013, 3, 1, 0, 0, 0), new LocalDateTime(2013, 3, 31, 23, 59, 59)))
		assert result != null
		assert result.lessons != null
		assert result.lessons.empty
	}
	
	@Test
	void getLessonsForTeacher_one_month() {
		def result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2013, 1, 1, 0, 0, 0), new LocalDateTime(2013, 1, 31, 23, 59, 59)))
		assert result != null
		assert result.lessons != null
		
		def student1 = new StudentSummary(1, "English", "A. Albert", new SchoolSummary(1, "My school 1", "#FF0000"))
		def student3 = new StudentSummary(3, "German", "C. Charles", new SchoolSummary(3, "My school 3", "#0000FF"))
		
		assert [
			new Lesson(2, student1, new LocalDate(2013,1,7), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new Lesson(3, student1, new LocalDate(2013,1,9), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new Lesson(6, student3, new LocalDate(2013,1,15), new LocalTime(9,0), new LocalTime(10,45), "Factory")
			] == result.lessons
	}
	
	@Test
	void getLessonsForTeacher_another_month() {
		def result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2013, 2, 1, 0, 0, 0), new LocalDateTime(2013, 2, 28, 23, 59, 59)))
		assert result != null
		assert result.lessons != null
		
		def student1 = new StudentSummary(1, "English", "A. Albert", new SchoolSummary(1, "My school 1", "#FF0000"))
		def student3 = new StudentSummary(3, "German", "C. Charles", new SchoolSummary(3, "My school 3", "#0000FF"))
		
		assert [
			new Lesson(4, student1, new LocalDate(2013,2,1), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new Lesson(5, student1, new LocalDate(2013,2,13), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new Lesson(7, student3, new LocalDate(2013,2,16), new LocalTime(9,0), new LocalTime(10,45), "Factory")
			] == result.lessons
	}
}
