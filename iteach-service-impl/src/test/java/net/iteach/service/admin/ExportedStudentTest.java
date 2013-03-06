package net.iteach.service.admin;

import net.iteach.api.model.copy.ExportedComment;
import net.iteach.api.model.copy.ExportedLesson;
import net.iteach.api.model.copy.ExportedStudent;
import net.iteach.service.config.DefaultConfiguration;
import net.iteach.test.Helper;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExportedStudentTest {

    private final ObjectMapper mapper = new DefaultConfiguration().jacksonObjectMapper();

    @Test
    public void read() throws IOException {
        String json = Helper.getResourceAsString("/net/iteach/service/admin/student.json");
        ExportedStudent student = mapper.readValue(json, ExportedStudent.class);
        assertNotNull(student);
        assertEquals("Mister Test", student.getName());
        assertEquals("Deutsch", student.getSubject());
        assertEquals(
                Arrays.asList(
                        new ExportedLesson(
                                Arrays.<ExportedComment>asList(),
                                new LocalDate(2013, 1, 15),
                                new LocalTime(12, 30),
                                new LocalTime(14, 0),
                                "Office"
                        ),
                        new ExportedLesson(
                                Arrays.<ExportedComment>asList(),
                                new LocalDate(2013, 1, 22),
                                new LocalTime(12, 30),
                                new LocalTime(14, 0),
                                "Office"
                        )
                ),
                student.getLessons());
    }

}
