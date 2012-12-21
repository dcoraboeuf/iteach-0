package net.iteach.service.impl

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.iteach.api.SchoolService;
import net.iteach.core.model.SchoolForm;
import net.iteach.test.AbstractIntegrationTest;

class SchoolServiceImplTest extends AbstractIntegrationTest {
	
	@Autowired
	private SchoolService service
	
	@Test
	void getSchools() {
		def schools = service.getSchoolsForTeacher(1)
		assert schools != null
		assert [1, 3] == schools.getSummaries()*.getId()
		assert ["My school 1", "My school 3"] == schools.getSummaries()*.getName()
	}
	
	@Test
	void createSchool() {
		def id = service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCC"));
		assert id != null
		assert id.isSuccess()
	}
	
	@Test
	void editSchool_name() {
		def ack = service.editSchoolForTeacher(1, 1, new SchoolForm("My school 11", "#FF0000"));
		assert ack != null
		assert ack.isSuccess()
		def schools = service.getSchoolsForTeacher(1)
		def school = schools.getSummaries().find { it.getId() == 1 }
		assert "My school 11" == school.getName()
		assert "#FF0000" == school.getColor()
	}
	
	@Test
	void editSchool_color() {
		def ack = service.editSchoolForTeacher(1, 3, new SchoolForm("My school 3", "#FFFF00"));
		assert ack != null
		assert ack.isSuccess()
		def schools = service.getSchoolsForTeacher(1)
		def school = schools.getSummaries().find { it.getId() == 3 }
		assert "My school 3" == school.getName()
		assert "#FFFF00" == school.getColor()
	}
	
	@Test(expected = SchoolNameAlreadyDefined.class)
	void editSchool_name_already_defined() {
		service.editSchoolForTeacher(1, 1, new SchoolForm("My school 3", "#FF0000"));
	}
	
	@Test(expected = SchoolNameAlreadyDefined.class)
	void createSchool_name_already_defined() {
		service.createSchoolForTeacher(1, new SchoolForm("My school 3", "#CCCCCC"));
	}

}
