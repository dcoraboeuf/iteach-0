package net.iteach.service.impl

import net.iteach.api.LessonService
import net.iteach.core.model.CoordinateType
import net.iteach.core.model.Coordinates
import net.iteach.core.model.Lesson
import net.iteach.core.model.LessonChange;
import net.iteach.core.model.LessonDetails
import net.iteach.core.model.LessonForm
import net.iteach.core.model.LessonRange
import net.iteach.core.model.SchoolSummary
import net.iteach.core.model.SchoolSummaryWithCoordinates
import net.iteach.core.model.StudentLesson
import net.iteach.core.model.StudentSummary
import net.iteach.core.model.StudentSummaryWithCoordinates
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
		service.getLessonsForStudent(2, 1, null, Locale.ENGLISH)
	}
	
	@Test
	void getLessonsForStudent() {
		def testDate = new LocalDate(2013, 1, 4)
		def studentLessons = service.getLessonsForStudent(1, 1, testDate, Locale.ENGLISH)
		assert studentLessons != null
		assert testDate == studentLessons.date
		assert 5 == studentLessons.hours
		def lessons = studentLessons.lessons
		assert lessons != null
		assert [
			new StudentLesson(2, new LocalDate(2013, 1, 7), new LocalTime(18,0), new LocalTime(20,30), "Home", "Jan 7, 2013", "6:00 PM", "8:30 PM"),
			new StudentLesson(3, new LocalDate(2013, 1, 9), new LocalTime(18,0), new LocalTime(20,30), "Home", "Jan 9, 2013", "6:00 PM", "8:30 PM")
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
		
		def student1 = new StudentSummary(1, "English", "A. Albert", new SchoolSummary(1, "My school 1", "#FF0000", MoneyUtils.money(10)))
		def student3 = new StudentSummary(3, "German", "C. Charles", new SchoolSummary(3, "My school 3", "#0000FF", MoneyUtils.money(30)))
		
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
		
		def student1 = new StudentSummary(1, "English", "A. Albert", new SchoolSummary(1, "My school 1", "#FF0000", MoneyUtils.money(10)))
		def student3 = new StudentSummary(3, "German", "C. Charles", new SchoolSummary(3, "My school 3", "#0000FF", MoneyUtils.money(30)))
		
		assert [
			new Lesson(4, student1, new LocalDate(2013,2,1), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new Lesson(5, student1, new LocalDate(2013,2,13), new LocalTime(18,0), new LocalTime(20,30), "Home"),
			new Lesson(7, student3, new LocalDate(2013,2,16), new LocalTime(9,0), new LocalTime(10,45), "Factory")
			] == result.lessons
	}
	
	@Test(expected = AccessDeniedException)
	void getLessonDetails_access_denied() {
		service.getLessonDetails(2, 1)
	}
	
	@Test
	void getLessonDetails() {
		def details = service.getLessonDetails(1, 1)
		assert new LessonDetails(
			1,
			new StudentSummaryWithCoordinates(
				1,
				"English",
				"A. Albert",
				new SchoolSummaryWithCoordinates(
					1,
					"My school 1",
					"#FF0000",
					MoneyUtils.money(10),
					Coordinates.create()
						.add(CoordinateType.ADDRESS, "At my school 1")
						.add(CoordinateType.WEB, "http://school/1")),
				Coordinates.create()
					.add(CoordinateType.MOBILE_PHONE, "0123456789")
					.add(CoordinateType.EMAIL, "albert@test.com")
				),
			new LocalDate(2012,12,21),
			new LocalTime(12,0),
			new LocalTime(13,0),
			"At school") == details
	}
	
	@Test(expected = AccessDeniedException)
	void createLessonForTeacher_access_denied() {
		service.createLessonForTeacher(
			2,
			new LessonForm(
				new LocalDate(2013,1,4),
				new LocalTime(11,0),
				new LocalTime(12,0),
				1,
				"Any location")
			) 
	}
	
	@Test
	void createLessonForTeacher() {
		def id = service.createLessonForTeacher(
			1,
			new LessonForm(
				new LocalDate(2013,1,4),
				new LocalTime(11,0),
				new LocalTime(12,0),
				1,
				"Any location")
			)
		assert id != null
		assert id.isSuccess()
	}
	
	@Test(expected = AccessDeniedException)
	void editLessonForTeacher_access_denied() {
		service.editLessonForTeacher(2, 1, 
			new LessonForm(
				new LocalDate(2013,1,4),
				new LocalTime(11,0),
				new LocalTime(12,0),
				1,
				"Any location")
			)
	}
	
	@Test
	void create_edit_delete() {
		// Creation
		def id = service.createLessonForTeacher(
			1,
			new LessonForm(
				new LocalDate(2013,4,1),
				new LocalTime(11,0),
				new LocalTime(12,0),
				1,
				"April's fool")
			)
		assert id != null
		assert id.isSuccess()
		// Edition
		def ack = service.editLessonForTeacher(1, id.getValue(), 
			new LessonForm(
				new LocalDate(2013,1,4),
				new LocalTime(11,0),
				new LocalTime(16,0),
				1,
				"At school")
			)
		assert ack != null
		assert ack.isSuccess()
		// Gets the details
		def details = service.getLessonDetails(1, id.getValue())
		assert "At school" == details.getLocation()
		assert new LocalTime(16,0) == details.getTo()
		// Deletes
		ack = service.deleteLessonForTeacher(1, id.getValue())
		assert ack != null
		assert ack.isSuccess()
	}
	
	@Test(expected = AccessDeniedException)
	void deleteLessonForTeacher_access_denied() {
		service.deleteLessonForTeacher(2, 1)
	}
	
	@Test(expected = AccessDeniedException)
	void changeLessonForTeacher_access_denied() {
		service.changeLessonForTeacher(2, 1, null)
	}
	
	@Test
	void changeLessonForTeacher_plus_minutes() {
		def id = service.createLessonForTeacher(1, new LessonForm(
			new LocalDate(2013,1,12),
			new LocalTime(11,0),
			new LocalTime(12,30),
			1,
			"Test"))
		assert id != null
		assert id.success
		def ack = service.changeLessonForTeacher(1, id.value, new LessonChange(0, 15))
		assert ack != null
		assert ack.success
		def lesson = service.getLessonDetails(1, id.value)
		assert new LocalDate(2013,1,12) == lesson.date
		assert new LocalTime(11,0) == lesson.from
		assert new LocalTime(12,45) == lesson.to		
	}
	
	@Test
	void changeLessonForTeacher_minus_minutes() {
		def id = service.createLessonForTeacher(1, new LessonForm(
			new LocalDate(2013,1,12),
			new LocalTime(11,0),
			new LocalTime(12,30),
			1,
			"Test"))
		assert id != null
		assert id.success
		def ack = service.changeLessonForTeacher(1, id.value, new LessonChange(0, -15))
		assert ack != null
		assert ack.success
		def lesson = service.getLessonDetails(1, id.value)
		assert new LocalDate(2013,1,12) == lesson.date
		assert new LocalTime(11,0) == lesson.from
		assert new LocalTime(12,15) == lesson.to		
	}
	
	@Test
	void changeLessonForTeacher_plus_days() {
		def id = service.createLessonForTeacher(1, new LessonForm(
			new LocalDate(2013,1,31),
			new LocalTime(11,0),
			new LocalTime(12,30),
			1,
			"Test"))
		assert id != null
		assert id.success
		def ack = service.changeLessonForTeacher(1, id.value, new LessonChange(1, 0))
		assert ack != null
		assert ack.success
		def lesson = service.getLessonDetails(1, id.value)
		assert new LocalDate(2013,2,1) == lesson.date
		assert new LocalTime(11,0) == lesson.from
		assert new LocalTime(12,30) == lesson.to		
	}
	
	@Test
	void changeLessonForTeacher_minus_days() {
		def id = service.createLessonForTeacher(1, new LessonForm(
			new LocalDate(2013,2,1),
			new LocalTime(11,0),
			new LocalTime(12,30),
			1,
			"Test"))
		assert id != null
		assert id.success
		def ack = service.changeLessonForTeacher(1, id.value, new LessonChange(-1, 0))
		assert ack != null
		assert ack.success
		def lesson = service.getLessonDetails(1, id.value)
		assert new LocalDate(2013,1,31) == lesson.date
		assert new LocalTime(11,0) == lesson.from
		assert new LocalTime(12,30) == lesson.to		
	}
	
}
