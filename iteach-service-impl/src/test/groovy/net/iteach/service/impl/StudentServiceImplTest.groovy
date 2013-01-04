package net.iteach.service.impl

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import net.iteach.api.StudentService
import net.iteach.core.model.CoordinateType
import net.iteach.core.model.Coordinates
import net.iteach.core.model.SchoolSummary
import net.iteach.core.model.StudentDetails
import net.iteach.core.model.StudentForm
import net.iteach.test.AbstractIntegrationTest

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException

class StudentServiceImplTest extends AbstractIntegrationTest {
	
	@Autowired
	private StudentService service
	
	@Test
	void getStudents() {
		def students = service.getStudentsForTeacher(1)
		assert students != null
		assert [1, 2, 3] == students.getSummaries()*.getId()
		assert ["English", "German", "German"] == students.getSummaries()*.getSubject()
		assert ["A. Albert", "B. Bernard", "C. Charles"] == students.getSummaries()*.getName()
		assert [1, 1, 3] == students.getSummaries()*.getSchool()*.getId()
	}
	
	@Test(expected = AccessDeniedException)
	void createStudent_access_denied() {
		service.createStudentForTeacher(2, new StudentForm(1, "English", "D. Dilbert", Coordinates.create()))
	}
	
	@Test
	void createStudent() {
		def id = service.createStudentForTeacher(1, new StudentForm(3, "English", "D. Dilbert", Coordinates.create()))
		assert id != null
		assert id.isSuccess()
	}
	
	@Test(expected = AccessDeniedException)
	void editStudent_access_denied() {
		service.editStudentForTeacher(2, 1, new StudentForm(1, "German", "A. Alfred", Coordinates.create()))
	}
	
	@Test
	void editStudent_name() {
		def ack = service.editStudentForTeacher(1, 1, new StudentForm(1, "German", "A. Alfred", Coordinates.create()))
		assert ack != null
		assert ack.isSuccess()
		def students = service.getStudentsForTeacher(1)
		def student = students.getSummaries().find { it.getId() == 1 }
		assert "A. Alfred" == student.getName()
	}
	
	@Test(expected = AccessDeniedException)
	void getStudentCoordinates_access_denied() {
		service.getStudentCoordinates(2, 1)
	}
	
	@Test
	void getStudentCoordinates () {
		def coordinates = service.getStudentCoordinates(1, 1)
		assert coordinates != null
		assert 2 == coordinates.getList().size()
		assert "0123456789" == coordinates.getCoordinateValue(CoordinateType.MOBILE_PHONE)
		assert "albert@test.com" == coordinates.getCoordinateValue(CoordinateType.EMAIL)
	}
	
	@Test
	void deleteStudent() {
		def id = service.createStudentForTeacher(1, new StudentForm(1, "English", "For Deletion", Coordinates.create()))
		assert id != null
		assert id.isSuccess()
		def ack = service.deleteStudentForTeacher(1, id.getValue())
		assert ack.isSuccess()
	}
	
	@Test(expected = AccessDeniedException.class)
	void deleteStudent_access_denied() {
		def id = service.createStudentForTeacher(1, new StudentForm(1, "English", "For Deletion and access denied", Coordinates.create()))
		assert id != null
		assert id.isSuccess()
		service.deleteStudentForTeacher(2, id.getValue())
	}
	
	@Test(expected = AccessDeniedException)
	void getStudentForTeacher_access_denied() {
		service.getStudentForTeacher(2, 1)
	}
	
	@Test
	void getStudentForTeacher() {
		def student = service.getStudentForTeacher(1, 1)
		assert new StudentDetails(
			1,
			"English",
			"A. Albert",
			Coordinates.create().add(CoordinateType.EMAIL, "albert@test.com").add(CoordinateType.MOBILE_PHONE, "0123456789"),
			new SchoolSummary(1, "My school 1", "#FF0000"),
			3.5) == student
	}
	
	@Test(expected = AccessDeniedException)
	void getStudentHours_access_denied() {
		service.getStudentHours(2, 1)
	}
	
	@Test
	void getStudentHours() {
		assert 3.5 == service.getStudentHours(1, 1)
	}

}
