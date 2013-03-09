package net.iteach.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.iteach.api.TeacherService;
import net.iteach.core.model.*;
import net.iteach.test.AbstractIntegrationTest;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;

public class StudentServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private TeacherService service;

    @Test
    public void getStudents() {
        StudentSummaries students = service.getStudentsForTeacher(1);
        assertNotNull(students);
        SchoolSummary school1 = new SchoolSummary(1, "My school 1", "#FF0000", MoneyUtils.money(new BigDecimal(10)));
        SchoolSummary school3 = new SchoolSummary(3, "My school 3", "#0000FF", MoneyUtils.money(new BigDecimal(30)));
        assertEquals(
                Arrays.asList(
                        new StudentSummary(1, "English", "A. Albert", school1, false),
                        new StudentSummary(2, "German", "B. Bernard", school1, false),
                        new StudentSummary(3, "German", "C. Charles", school3, false)
                ),
                students.getSummaries()
        );
    }

    @Test(expected = AccessDeniedException.class)
    public void createStudent_access_denied() {
        service.createStudentForTeacher(2, new StudentForm(1, "English", "D. Dilbert", Coordinates.create()));
    }

    @Test
    public void createStudent() {
        ID id = service.createStudentForTeacher(1, new StudentForm(3, "English", "D. Dilbert", Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());
    }

    @Test(expected = AccessDeniedException.class)
    public void editStudent_access_denied() {
        service.editStudentForTeacher(2, 1, new StudentForm(1, "German", "A. Alfred", Coordinates.create()));
    }

    protected Predicate<StudentSummary> idPredicate(final int id) {
        return new Predicate<StudentSummary>() {
            @Override
            public boolean apply(StudentSummary it) {
                return it.getId() == id;
            }
        };
    }

    @Test
    public void editStudent_name() {
        Ack ack = service.editStudentForTeacher(1, 1, new StudentForm(1, "German", "A. Alfred", Coordinates.create()));
        assertNotNull(ack);
        assertTrue(ack.isSuccess());
        StudentSummaries students = service.getStudentsForTeacher(1);
        StudentSummary student = Iterables.find(students.getSummaries(), idPredicate(1));
        assertEquals("A. Alfred", student.getName());
    }

    @Test(expected = AccessDeniedException.class)
    public void getStudentCoordinates_access_denied() {
        service.getStudentCoordinates(2, 1);
    }

    @Test
    public void getStudentCoordinates() {
        Coordinates coordinates = service.getStudentCoordinates(1, 1);
        assertNotNull(coordinates);
        assertEquals(2, coordinates.getList().size());
        assertEquals("0123456789", coordinates.getCoordinateValue(CoordinateType.MOBILE_PHONE));
        assertEquals("albert@test.com", coordinates.getCoordinateValue(CoordinateType.EMAIL));
    }

    @Test
    public void deleteStudent() {
        ID id = service.createStudentForTeacher(1, new StudentForm(1, "English", "For Deletion", Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        Ack ack = service.deleteStudentForTeacher(1, id.getValue());
        assertTrue(ack.isSuccess());
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteStudent_access_denied() {
        ID id = service.createStudentForTeacher(1, new StudentForm(1, "English", "For Deletion and access denied", Coordinates.create()));
        assertNotNull(id);
        assertTrue(id.isSuccess());
        service.deleteStudentForTeacher(2, id.getValue());
    }

    @Test(expected = AccessDeniedException.class)
    public void getStudentForTeacher_access_denied() {
        service.getStudentForTeacher(2, 1);
    }

    @Test
    public void getStudentForTeacher() {
        StudentDetails student = service.getStudentForTeacher(1, 1);
        assertEquals(
                new StudentDetails(
                        1,
                        "English",
                        "A. Albert",
                        Coordinates.create().add(CoordinateType.EMAIL, "albert@test.com").add(CoordinateType.MOBILE_PHONE, "0123456789"),
                        new SchoolSummary(1, "My school 1", "#FF0000", Money.of(CurrencyUnit.EUR, 10.0)),
                        new BigDecimal("3.50"),
                        false),
                student);
    }

    @Test(expected = AccessDeniedException.class)
    public void getStudentHours_access_denied() {
        service.getStudentHours(2, 1);
    }

    @Test
    public void getStudentHours() {
        assertEquals(new BigDecimal("3.50"), service.getStudentHours(1, 1));
    }

}
