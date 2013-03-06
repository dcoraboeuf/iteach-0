package net.iteach.service.admin;

import net.iteach.api.model.copy.ExportedLesson;
import net.iteach.service.config.DefaultConfiguration;
import net.iteach.test.Helper;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExportedLessonTest {

    private final ObjectMapper mapper = new DefaultConfiguration().jacksonObjectMapper();

    @Test
    public void read() throws IOException {
        String json = Helper.getResourceAsString("/net/iteach/service/admin/lesson.json");
        ExportedLesson lesson = mapper.readValue(json, ExportedLesson.class);
        assertNotNull(lesson);
        assertEquals(new LocalDate(2013, 1, 15), lesson.getDate());
        assertEquals(new LocalTime(12, 30), lesson.getFrom());
        assertEquals(new LocalTime(14, 0), lesson.getTo());
        assertEquals("Office", lesson.getLocation());
    }

}
