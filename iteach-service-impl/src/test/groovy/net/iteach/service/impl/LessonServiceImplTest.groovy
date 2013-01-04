package net.iteach.service.impl

import net.iteach.api.LessonService
import net.iteach.core.model.StudentLesson
import net.iteach.test.AbstractIntegrationTest

import org.joda.time.LocalDate
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
}
