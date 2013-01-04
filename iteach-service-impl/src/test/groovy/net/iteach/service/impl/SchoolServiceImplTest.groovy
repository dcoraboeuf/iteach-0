package net.iteach.service.impl

import net.iteach.api.SchoolService
import net.iteach.core.model.CoordinateType
import net.iteach.core.model.Coordinates
import net.iteach.core.model.SchoolDetails
import net.iteach.core.model.SchoolDetailsStudent
import net.iteach.core.model.SchoolForm
import net.iteach.core.validation.ValidationException
import net.iteach.test.AbstractIntegrationTest
import net.sf.jstring.Strings
import net.sf.jstring.support.StringsLoader

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException

class SchoolServiceImplTest extends AbstractIntegrationTest {

    private static Strings strings = StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
	
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
		def id = service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCC", Coordinates.create()));
		assert id != null
		assert id.isSuccess()
	}
	
	@Test
	void deleteSchool() {
		def id = service.createSchoolForTeacher(2, new SchoolForm("Test for delete", "#CCCCCC", Coordinates.create()));
		assert id != null
		assert id.isSuccess()
		def ack = service.deleteSchoolForTeacher(2, id.getValue())
		assert ack.isSuccess()
	}
	
	@Test(expected = AccessDeniedException.class)
	void deleteSchool_access_denied() {
		def id = service.createSchoolForTeacher(2, new SchoolForm("Test for delete and access denied", "#CCCCCC", Coordinates.create()));
		assert id != null
		assert id.isSuccess()
		service.deleteSchoolForTeacher(1, id.getValue())
	}

    def validation (Closure<Void> closure, String expectedMessage) {
        try {
            closure.run();
            assert false: "Expected validation error"
        } catch (ValidationException ex) {
            def message = ex.getLocalizedMessage(strings, Locale.ENGLISH)
            assert expectedMessage == message
        }
    }

    @Test
    void school_no_name () {
        validation( { service.createSchoolForTeacher(2, new SchoolForm(null, "#CCCCCC", Coordinates.create())) },
                " - School name: may not be null\n")
    }

    @Test
    void school_tooshort_name () {
        validation( { service.createSchoolForTeacher(2, new SchoolForm("", "#CCCCCC", Coordinates.create())) },
                " - School name: size must be between 1 and 80\n")
    }

    @Test
    void school_toolong_name () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("x" * 81, "#CCCCCC", Coordinates.create())) },
                " - School name: size must be between 1 and 80\n")
    }

    @Test
    void school_no_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", null, Coordinates.create())) },
                " - Colour code for the school: may not be null\n")
    }

    @Test
    void school_tooshort_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCC", Coordinates.create())) },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n")
    }

    @Test
    void school_toolong_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCCC", Coordinates.create())) },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n")
    }

    @Test
    void school_wrong_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", "#55CCMM", Coordinates.create())) },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n")
    }
	
	@Test(expected = AccessDeniedException.class)
	void editSchool_access_denied() {
		service.editSchoolForTeacher(2, 1, new SchoolForm("My school 11", "#ff0000", Coordinates.create()));
	}
	
	@Test
	void editSchool_name() {
		def ack = service.editSchoolForTeacher(1, 1, new SchoolForm("My school 11", "#ff0000", Coordinates.create()));
		assert ack != null
		assert ack.isSuccess()
		def schools = service.getSchoolsForTeacher(1)
		def school = schools.getSummaries().find { it.getId() == 1 }
		assert "My school 11" == school.getName()
		assert "#ff0000" == school.getColor()
	}
	
	@Test
	void editSchool_color() {
		def ack = service.editSchoolForTeacher(1, 3, new SchoolForm("My school 3", "#FFFF00", Coordinates.create()));
		assert ack != null
		assert ack.isSuccess()
		def schools = service.getSchoolsForTeacher(1)
		def school = schools.getSummaries().find { it.getId() == 3 }
		assert "My school 3" == school.getName()
		assert "#FFFF00" == school.getColor()
	}
	
	@Test(expected = SchoolNameAlreadyDefined.class)
	void editSchool_name_already_defined() {
		service.editSchoolForTeacher(1, 1, new SchoolForm("My school 3", "#FF0000", Coordinates.create()));
	}
	
	@Test(expected = SchoolNameAlreadyDefined.class)
	void createSchool_name_already_defined() {
		service.createSchoolForTeacher(1, new SchoolForm("My school 3", "#CCCCCC", Coordinates.create()));
	}
	
	@Test(expected = AccessDeniedException.class)
	void getSchoolCoordinates_access_denied () {
		service.getSchoolCoordinates(2, 1)
	}
	
	@Test
	void getSchoolCoordinates () {
		def coordinates = service.getSchoolCoordinates(1, 1)
		assert coordinates != null
		assert 2 == coordinates.getList().size()
		assert "At my school 1" == coordinates.getCoordinateValue(CoordinateType.ADDRESS)
		assert "http://school/1" == coordinates.getCoordinateValue(CoordinateType.WEB)
	}
	
	@Test(expected = AccessDeniedException)
	void getSchoolForTeacher_access_denied() {
		service.getSchoolForTeacher(2, 1)
	}
	
	@Test
	void getSchoolForTeacher() {
		def school = service.getSchoolForTeacher(1, 1)
		assert new SchoolDetails(
			1,
			"My school 1",
			"#FF0000",
			Coordinates.create().add(CoordinateType.ADDRESS, "At my school 1").add(CoordinateType.WEB, "http://school/1"),
			[
				new SchoolDetailsStudent(1, "Student 1", "Subject 1", 0),
				new SchoolDetailsStudent(2, "Student 2", "Subject 2", 0)
			]) == school
	}

}
