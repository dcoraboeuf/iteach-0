package net.iteach.service.impl

import net.iteach.api.StudentService
import net.iteach.core.model.SchoolForm
import net.iteach.core.model.StudentForm;
import net.iteach.test.AbstractIntegrationTest

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

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
	
	@Test
	void createStudent() {
		def id = service.createStudentForTeacher(1, new StudentForm(3, "English", "D. Dilbert"))
		assert id != null
		assert id.isSuccess()
	}
	
	@Test
	void editStudent_name() {
		def ack = service.editStudentForTeacher(1, 1, new StudentForm(1, "German", "A. Alfred"))
		assert ack != null
		assert ack.isSuccess()
		def students = service.getStudentsForTeacher(1)
		def student = students.getSummaries().find { it.getId() == 1 }
		assert "A. Alfred" == student.getName()
	}
	
	// TODO Test deletion
	// TODO Test link teacher<->student on create, edit and delete

}
