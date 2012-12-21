package net.iteach.service.impl

import net.iteach.api.SchoolService
import net.iteach.core.model.SchoolForm
import net.iteach.core.validation.ValidationException
import net.iteach.test.AbstractIntegrationTest
import net.sf.jstring.Strings
import net.sf.jstring.support.StringsLoader
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

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
		def id = service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCC"));
		assert id != null
		assert id.isSuccess()
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
        validation( { service.createSchoolForTeacher(2, new SchoolForm(null, "#CCCCCC")) },
                " - School name: may not be null\n")
    }

    @Test
    void school_tooshort_name () {
        validation( { service.createSchoolForTeacher(2, new SchoolForm("", "#CCCCCC")) },
                " - School name: size must be between 1 and 80\n")
    }

    @Test
    void school_toolong_name () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("x" * 81, "#CCCCCC")) },
                " - School name: size must be between 1 and 80\n")
    }

    @Test
    void school_no_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", null)) },
                " - Colour code for the school: may not be null\n")
    }

    @Test
    void school_tooshort_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCC")) },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n")
    }

    @Test
    void school_toolong_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCCC")) },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n")
    }

    @Test
    void school_wrong_color () {
        validation ( { service.createSchoolForTeacher(2, new SchoolForm("Test", "#55CCMM")) },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n")
    }
	
	@Test
	void editSchool_name() {
		def ack = service.editSchoolForTeacher(1, 1, new SchoolForm("My school 11", "#ff0000"));
		assert ack != null
		assert ack.isSuccess()
		def schools = service.getSchoolsForTeacher(1)
		def school = schools.getSummaries().find { it.getId() == 1 }
		assert "My school 11" == school.getName()
		assert "#ff0000" == school.getColor()
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
