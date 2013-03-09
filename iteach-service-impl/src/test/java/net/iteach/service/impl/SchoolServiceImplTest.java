package net.iteach.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.iteach.api.TeacherService;
import net.iteach.core.model.*;
import net.iteach.core.validation.ValidationException;
import net.iteach.test.AbstractIntegrationTest;
import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class SchoolServiceImplTest extends AbstractIntegrationTest {

    private static Strings strings = StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);

    @Autowired
    private TeacherService service;

    @Test
    public void getSchools() {
        SchoolSummaries schools = service.getSchoolsForTeacher(1);
        assertNotNull(schools);
        assertEquals(asList(1, 3),
                Lists.transform(schools.getSummaries(), new Function<SchoolSummary, Integer>() {
                    @Override
                    public Integer apply(SchoolSummary it) {
                        return it.getId();
                    }
                }));
        assertEquals(asList("My school 1", "My school 3"),
                Lists.transform(schools.getSummaries(), new Function<SchoolSummary, String>() {
                    @Override
                    public String apply(SchoolSummary it) {
                        return it.getName();
                    }
                }));
    }

    @Test
    public void createSchool() {
        ID id = service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCC", new BigDecimal(10), Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());
    }

    @Test
    public void deleteSchool() {
        ID id = service.createSchoolForTeacher(2, new SchoolForm("Test for delete", "#CCCCCC", new BigDecimal(10), Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        Ack ack = service.deleteSchoolForTeacher(2, id.getValue());
        assertTrue(ack.isSuccess());
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteSchool_access_denied() {
        ID id = service.createSchoolForTeacher(2, new SchoolForm("Test for delete and access denied", "#CCCCCC", new BigDecimal(12), Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        service.deleteSchoolForTeacher(1, id.getValue());
    }

    protected void validation(Runnable closure, String expectedMessage) {
        try {
            closure.run();
            fail("Expected validation error");
        } catch (ValidationException ex) {
            String message = ex.getLocalizedMessage(strings, Locale.ENGLISH);
            assertEquals(expectedMessage, message);
        }
    }

    @Test
    public void school_no_name() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm(null, "#CCCCCC", BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - School name: may not be null\n"
        );
    }

    @Test
    public void school_tooshort_name() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm("", "#CCCCCC", BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - School name: size must be between 1 and 80\n");
    }

    @Test
    public void school_toolong_name() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm(StringUtils.repeat("x", 81), "#CCCCCC", BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - School name: size must be between 1 and 80\n");
    }

    @Test
    public void school_no_color() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm("Test", null, BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - Colour code for the school: may not be null\n");
    }

    @Test
    public void school_tooshort_color() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCC", BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n");
    }

    @Test
    public void school_toolong_color() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm("Test", "#CCCCCCC", BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n");
    }

    @Test
    public void school_wrong_color() {
        validation(
                new Runnable() {
                    @Override
                    public void run() {
                        service.createSchoolForTeacher(2, new SchoolForm("Test", "#55CCMM", BigDecimal.ZERO, Coordinates.create()));
                    }
                },
                " - Colour code for the school: must match \"#[0-9A-Fa-f]{6}\"\n");
    }

    @Test(expected = AccessDeniedException.class)
    public void editSchool_access_denied() {
        service.editSchoolForTeacher(2, 1, new SchoolForm("My school 11", "#ff0000", BigDecimal.ZERO, Coordinates.create()));
    }

    protected Predicate<SchoolSummary> idPredicate(final int id) {
        return new Predicate<SchoolSummary>() {
            @Override
            public boolean apply(SchoolSummary it) {
                return it.getId() == id;
            }
        };
    }

    @Test
    public void editSchool_name() {
        Ack ack = service.editSchoolForTeacher(1, 1, new SchoolForm("My school 11", "#ff0000", new BigDecimal(10), Coordinates.create()));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        SchoolSummaries schools = service.getSchoolsForTeacher(1);
        SchoolSummary school = Iterables.find(schools.getSummaries(), idPredicate(1));
        assertEquals("My school 11", school.getName());
        assertEquals("#ff0000", school.getColor());
        assertEquals("EUR 10.00", school.getHourlyRate().toString());
    }

    @Test
    public void editSchool_color() {
        Ack ack = service.editSchoolForTeacher(1, 3, new SchoolForm("My school 3", "#FFFF00", new BigDecimal(10), Coordinates.create()));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        SchoolSummaries schools = service.getSchoolsForTeacher(1);
        SchoolSummary school = Iterables.find(schools.getSummaries(), idPredicate(3));
        assertEquals("My school 3", school.getName());
        assertEquals("#FFFF00", school.getColor());
        assertEquals("EUR 10.00", school.getHourlyRate().toString());
    }

    @Test
    public void editSchool_hourlyRate() {
        Ack ack = service.editSchoolForTeacher(1, 3, new SchoolForm("My school 3", "#FFFF00", new BigDecimal(20), Coordinates.create()));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        SchoolSummaries schools = service.getSchoolsForTeacher(1);
        SchoolSummary school = Iterables.find(schools.getSummaries(), idPredicate(3));
        assertEquals("My school 3", school.getName());
        assertEquals("#FFFF00", school.getColor());
        assertEquals("EUR 20.00", school.getHourlyRate().toString());
    }

    @Test(expected = SchoolNameAlreadyDefined.class)
    public void editSchool_name_already_defined() {
        service.editSchoolForTeacher(1, 1, new SchoolForm("My school 3", "#FF0000", BigDecimal.ZERO, Coordinates.create()));
    }

    @Test(expected = SchoolNameAlreadyDefined.class)
    public void createSchool_name_already_defined() {
        service.createSchoolForTeacher(1, new SchoolForm("My school 3", "#CCCCCC", BigDecimal.ZERO, Coordinates.create()));
    }

    @Test(expected = AccessDeniedException.class)
    public void getSchoolCoordinates_access_denied() {
        service.getSchoolCoordinates(2, 1);
    }

    @Test
    public void getSchoolCoordinates() {
        Coordinates coordinates = service.getSchoolCoordinates(1, 1);
        assertNotNull(coordinates);
        assertEquals(2, coordinates.getList().size());
        assertEquals("At my school 1", coordinates.getCoordinateValue(CoordinateType.ADDRESS));
        assertEquals("http://school/1", coordinates.getCoordinateValue(CoordinateType.WEB));
    }

    @Test(expected = AccessDeniedException.class)
    public void getSchoolForTeacher_access_denied() {
        service.getSchoolForTeacher(2, 1);
    }

    @Test
    public void getSchoolForTeacher() {
        SchoolDetails school = service.getSchoolForTeacher(1, 1);
        assertEquals(
                new SchoolDetails(
                        1,
                        "My school 1",
                        "#FF0000",
                        MoneyUtils.money(new BigDecimal(10)),
                        Coordinates.create().add(CoordinateType.ADDRESS, "At my school 1").add(CoordinateType.WEB, "http://school/1"),
                        Arrays.asList(
                                new SchoolDetailsStudent(1, "Student 1", "Subject 1", false, BigDecimal.ZERO),
                                new SchoolDetailsStudent(2, "Student 2", "Subject 2", false, BigDecimal.ZERO))
                        ,
                        BigDecimal.ZERO),
                school);
    }

    @Test
    public void comments() {
        // Creates a school for tests
        ID id = service.createSchoolForTeacher(1, new SchoolForm("School for comments", "#CCCCCC", BigDecimal.ZERO, Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());

        // Adds some comments
        // 1
        Comment comment1 = service.editSchoolComment(1, id.getValue(), CommentFormat.HTML, new CommentsForm(0, "*Bold* comment"));
        assertNotNull(comment1);
        assertEquals("<b>Bold</b> comment", comment1.getContent());
        // 2
        Comment comment2 = service.editSchoolComment(1, id.getValue(), CommentFormat.HTML, new CommentsForm(0, "_Italic_ comment"));
        assertNotNull(comment2);
        assertEquals("<i>Italic</i> comment", comment2.getContent());

        // Gets all the comments
        Comments comments = service.getSchoolComments(1, id.getValue(), 0, 10, 50, CommentFormat.RAW);
        assertNotNull(comments);
        assertFalse(comments.isMore());
        assertEquals(2, comments.getList().size());
        assertEquals("_Italic_ comment", comments.getList().get(0).getContent());
        assertEquals("*Bold* comment", comments.getList().get(1).getContent());

        // Deletes the school
        Ack ack = service.deleteSchoolForTeacher(1, id.getValue());
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
    }

}
