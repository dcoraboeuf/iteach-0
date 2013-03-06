package net.iteach.service.admin;

import net.iteach.api.model.copy.ExportedComment;
import net.iteach.api.model.copy.ExportedLesson;
import net.iteach.api.model.copy.ExportedSchool;
import net.iteach.api.model.copy.ExportedStudent;
import net.iteach.core.model.Coordinate;
import net.iteach.core.model.CoordinateType;
import net.iteach.service.config.DefaultConfiguration;
import net.iteach.test.Helper;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExportedSchoolTest {

    private final ObjectMapper mapper = new DefaultConfiguration().jacksonObjectMapper();

    @Test
    public void read() throws IOException {
        String json = Helper.getResourceAsString("/net/iteach/service/admin/school.json");
        ExportedSchool school = mapper.readValue(json, ExportedSchool.class);
        assertNotNull(school);
        assertEquals("Big School", school.getName());
        assertEquals("#ff80ff", school.getColor());
        assertEquals(new BigDecimal("27.00"), school.getHrate());
        assertEquals("EUR", school.getCurrency());
        assertEquals(
                Arrays.asList(
                        new ExportedStudent(
                                Arrays.asList(
                                        new ExportedComment(
                                                new DateTime(2013, 2, 13, 14, 27, 11, DateTimeZone.UTC),
                                                null,
                                                "Some comments"
                                        )
                                ),
                                Arrays.asList(
                                        new Coordinate(CoordinateType.EMAIL, "test@test.com")
                                ),
                                "Mister Test",
                                "Deutsch",
                                false,
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
                                )
                        )
                ),
                school.getStudents()
        );
    }

}
