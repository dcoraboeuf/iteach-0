package net.iteach.service.impl

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.iteach.api.SchoolService;
import net.iteach.test.AbstractIntegrationTest;

class SchoolServiceImplTest extends AbstractIntegrationTest {
	
	@Autowired
	private SchoolService service
	
	@Test
	@Ignore
	void getSchools() {
		def schools = service.getSchoolsForTeacher(1)
		assert schools != null
		assert [1, 3] == schools.getSummaries()*.getId()
		assert ["My school 1", "My school 3"] == schools.getSummaries()*.getName()
	}

}
