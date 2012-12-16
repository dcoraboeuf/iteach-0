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

}
