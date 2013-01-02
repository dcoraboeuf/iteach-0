package net.iteach.core.model;

import com.netbeetle.jackson.ObjectMapperFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StudentFormTest {

    @Test
    public void to_json() throws IOException {
        StudentForm form = new StudentForm(
                1,
                "My subject",
                "My name",
                Coordinates.create()
                    .add(CoordinateType.EMAIL, "mymail@test.com")
                    .add(CoordinateType.PHONE, "00 123 456 789"));
        ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
        String json = mapper.writeValueAsString(form);
        assertEquals ("{\"school\":1,\"subject\":\"My subject\",\"name\":\"My name\",\"coordinates\":{\"map\":{\"PHONE\":\"00 123 456 789\",\"EMAIL\":\"mymail@test.com\"}}}", json);
    }

    @Test
    public void from_json() throws IOException {
        String json = "{\"school\":1,\"subject\":\"My subject\",\"name\":\"My name\",\"coordinates\":{\"map\":{\"PHONE\":\"00 123 456 789\",\"EMAIL\":\"mymail@test.com\"}}}";
        ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
        StudentForm actualForm = mapper.readValue(json, StudentForm.class);
        StudentForm expectedForm = new StudentForm(
                1,
                "My subject",
                "My name",
                Coordinates.create()
                        .add(CoordinateType.EMAIL, "mymail@test.com")
                        .add(CoordinateType.PHONE, "00 123 456 789"));
        assertEquals(expectedForm, actualForm);
    }

}
