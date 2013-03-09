package net.iteach.service.impl;

import net.iteach.api.TeacherService;
import net.iteach.core.model.*;
import net.iteach.test.AbstractIntegrationTest;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class LessonServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private TeacherService service;

    @Test(expected = AccessDeniedException.class)
    public void getLessonsForStudent_access_denied() {
        service.getLessonsForStudent(2, 1, null, Locale.ENGLISH);
    }

    @Test
    public void getLessonsForStudent() {
        LocalDate testDate = new LocalDate(2013, 1, 4);
        StudentLessons studentLessons = service.getLessonsForStudent(1, 1, testDate, Locale.ENGLISH);
        assertNotNull(studentLessons);
        assertEquals(testDate, studentLessons.getDate());
        assertEquals(new BigDecimal("5.00"), studentLessons.getHours());
        List<StudentLesson> lessons = studentLessons.getLessons();
        assertNotNull(lessons);
        assertEquals(asList(
                new StudentLesson(2, new LocalDate(2013, 1, 7), new LocalTime(18, 0), new LocalTime(20, 30), "Home", "Jan 7, 2013", "6:00 PM", "8:30 PM"),
                new StudentLesson(3, new LocalDate(2013, 1, 9), new LocalTime(18, 0), new LocalTime(20, 30), "Home", "Jan 9, 2013", "6:00 PM", "8:30 PM")
        ),
                lessons);
    }

    @Test
    public void getLessonsForTeacher_before() {
        Lessons result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2012, 11, 1, 0, 0, 0), new LocalDateTime(2012, 11, 30, 23, 59, 59)));
        assertNotNull(result);
        assertNotNull(result.getLessons());
        assertTrue(result.getLessons().isEmpty());
    }

    @Test
    public void getLessonsForTeacher_after() {
        Lessons result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2013, 3, 1, 0, 0, 0), new LocalDateTime(2013, 3, 31, 23, 59, 59)));
        assertNotNull(result);
        assertNotNull(result.getLessons());
        assertTrue(result.getLessons().isEmpty());
    }

    @Test
    public void getLessonsForTeacher_one_month() {
        Lessons result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2013, 1, 1, 0, 0, 0), new LocalDateTime(2013, 1, 31, 23, 59, 59)));
        assertNotNull(result);
        assertNotNull(result.getLessons());

        StudentSummary student1 = new StudentSummary(1, "English", "A. Albert", new SchoolSummary(1, "My school 1", "#FF0000", MoneyUtils.money(new BigDecimal(10))), false);
        StudentSummary student3 = new StudentSummary(3, "German", "C. Charles", new SchoolSummary(3, "My school 3", "#0000FF", MoneyUtils.money(new BigDecimal(30))), false);

        assertEquals(asList(
                new Lesson(2, student1, new LocalDate(2013, 1, 7), new LocalTime(18, 0), new LocalTime(20, 30), "Home"),
                new Lesson(3, student1, new LocalDate(2013, 1, 9), new LocalTime(18, 0), new LocalTime(20, 30), "Home"),
                new Lesson(6, student3, new LocalDate(2013, 1, 15), new LocalTime(9, 0), new LocalTime(10, 45), "Factory")
        ),
                result.getLessons());
    }

    @Test
    public void getLessonsForTeacher_another_month() {
        Lessons result = service.getLessonsForTeacher(1, new LessonRange(new LocalDateTime(2013, 2, 1, 0, 0, 0), new LocalDateTime(2013, 2, 28, 23, 59, 59)));
        assertNotNull(result);
        assertNotNull(result.getLessons());

        StudentSummary student1 = new StudentSummary(1, "English", "A. Albert", new SchoolSummary(1, "My school 1", "#FF0000", MoneyUtils.money(new BigDecimal(10))), false);
        StudentSummary student3 = new StudentSummary(3, "German", "C. Charles", new SchoolSummary(3, "My school 3", "#0000FF", MoneyUtils.money(new BigDecimal(30))), false);

        assertEquals(asList(
                new Lesson(4, student1, new LocalDate(2013, 2, 1), new LocalTime(18, 0), new LocalTime(20, 30), "Home"),
                new Lesson(5, student1, new LocalDate(2013, 2, 13), new LocalTime(18, 0), new LocalTime(20, 30), "Home"),
                new Lesson(7, student3, new LocalDate(2013, 2, 16), new LocalTime(9, 0), new LocalTime(10, 45), "Factory")),
                result.getLessons());
    }

    @Test(expected = AccessDeniedException.class)
    public void getLessonDetails_access_denied() {
        service.getLessonDetails(2, 1);
    }

    @Test
    public void getLessonDetails() {
        LessonDetails details = service.getLessonDetails(1, 1);
        SchoolSummaryWithCoordinates school = new SchoolSummaryWithCoordinates(
                new SchoolSummary(
                        1,
                        "My school 1",
                        "#FF0000",
                        MoneyUtils.money(new BigDecimal(10))),
                Coordinates.create()
                        .add(CoordinateType.ADDRESS, "At my school 1")
                        .add(CoordinateType.WEB, "http://school/1"));
        assertEquals(new LessonDetails(
                1,
                new StudentSummaryWithCoordinates(
                        new StudentSummary(
                                1,
                                "English",
                                "A. Albert",
                                school,
                                false),
                        school,
                        Coordinates.create()
                                .add(CoordinateType.MOBILE_PHONE, "0123456789")
                                .add(CoordinateType.EMAIL, "albert@test.com")
                ),
                new LocalDate(2012, 12, 21),
                new LocalTime(12, 0),
                new LocalTime(13, 0),
                "At school"), details);
    }

    @Test(expected = AccessDeniedException.class)
    public void createLessonForTeacher_access_denied() {
        service.createLessonForTeacher(
                2,
                new LessonForm(
                        new LocalDate(2013, 1, 4),
                        new LocalTime(11, 0),
                        new LocalTime(12, 0),
                        1,
                        "Any location")
        );
    }

    @Test
    public void createLessonForTeacher() {
        ID id = service.createLessonForTeacher(
                1,
                new LessonForm(
                        new LocalDate(2013, 1, 4),
                        new LocalTime(11, 0),
                        new LocalTime(12, 0),
                        1,
                        "Any location")
        );
        assertNotNull(id);
        assertTrue(id.isSuccess());
    }

    @Test(expected = AccessDeniedException.class)
    public void editLessonForTeacher_access_denied() {
        service.editLessonForTeacher(2, 1,
                new LessonForm(
                        new LocalDate(2013, 1, 4),
                        new LocalTime(11, 0),
                        new LocalTime(12, 0),
                        1,
                        "Any location")
        );
    }

    @Test
    public void create_edit_delete() {
        // Creation
        ID id = service.createLessonForTeacher(
                1,
                new LessonForm(
                        new LocalDate(2013, 4, 1),
                        new LocalTime(11, 0),
                        new LocalTime(12, 0),
                        1,
                        "April's fool")
        );
        assertNotNull(id);
        assertTrue(id.isSuccess());
        // Edition
        Ack ack = service.editLessonForTeacher(1, id.getValue(),
                new LessonForm(
                        new LocalDate(2013, 1, 4),
                        new LocalTime(11, 0),
                        new LocalTime(16, 0),
                        1,
                        "At school")
        );
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        // Gets the details
        LessonDetails details = service.getLessonDetails(1, id.getValue());
        assertEquals("At school", details.getLocation());
        assertEquals(new LocalTime(16, 0), details.getTo());
        // Deletes
        ack = service.deleteLessonForTeacher(1, id.getValue());
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteLessonForTeacher_access_denied() {
        service.deleteLessonForTeacher(2, 1);
    }

    @Test(expected = AccessDeniedException.class)
    public void changeLessonForTeacher_access_denied() {
        service.changeLessonForTeacher(2, 1, null);
    }

    @Test
    public void changeLessonForTeacher_plus_minutes() {
        ID id = service.createLessonForTeacher(1, new LessonForm(
                new LocalDate(2013, 1, 12),
                new LocalTime(11, 0),
                new LocalTime(12, 30),
                1,
                "Test"));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        Ack ack = service.changeLessonForTeacher(1, id.getValue(), new LessonChange(0, 15));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        LessonDetails lesson = service.getLessonDetails(1, id.getValue());
        assertEquals(new LocalDate(2013, 1, 12), lesson.getDate());
        assertEquals(new LocalTime(11, 0), lesson.getFrom());
        assertEquals(new LocalTime(12, 45), lesson.getTo());
    }

    @Test
    public void changeLessonForTeacher_minus_minutes() {
        ID id = service.createLessonForTeacher(1, new LessonForm(
                new LocalDate(2013, 1, 12),
                new LocalTime(11, 0),
                new LocalTime(12, 30),
                1,
                "Test"));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        Ack ack = service.changeLessonForTeacher(1, id.getValue(), new LessonChange(0, -15));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        LessonDetails lesson = service.getLessonDetails(1, id.getValue());
        assertEquals(new LocalDate(2013, 1, 12), lesson.getDate());
        assertEquals(new LocalTime(11, 0), lesson.getFrom());
        assertEquals(new LocalTime(12, 15), lesson.getTo());
    }

    @Test
    public void changeLessonForTeacher_plus_days() {
        ID id = service.createLessonForTeacher(1, new LessonForm(
                new LocalDate(2013, 1, 31),
                new LocalTime(11, 0),
                new LocalTime(12, 30),
                1,
                "Test"));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        Ack ack = service.changeLessonForTeacher(1, id.getValue(), new LessonChange(1, 0));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        LessonDetails lesson = service.getLessonDetails(1, id.getValue());
        assertEquals(new LocalDate(2013, 2, 1), lesson.getDate());
        assertEquals(new LocalTime(11, 0), lesson.getFrom());
        assertEquals(new LocalTime(12, 30), lesson.getTo());
    }

    @Test
    public void changeLessonForTeacher_minus_days() {
        ID id = service.createLessonForTeacher(1, new LessonForm(
                new LocalDate(2013, 2, 1),
                new LocalTime(11, 0),
                new LocalTime(12, 30),
                1,
                "Test"));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        Ack ack = service.changeLessonForTeacher(1, id.getValue(), new LessonChange(-1, 0));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        LessonDetails lesson = service.getLessonDetails(1, id.getValue());
        assertEquals(new LocalDate(2013, 1, 31), lesson.getDate());
        assertEquals(new LocalTime(11, 0), lesson.getFrom());
        assertEquals(new LocalTime(12, 30), lesson.getTo());
    }

}
