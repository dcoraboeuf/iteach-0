package net.iteach.service.admin;

import net.iteach.api.model.copy.ExportedComment;
import net.iteach.service.config.DefaultConfiguration;
import net.iteach.test.Helper;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExportedCommentTest {

    private final ObjectMapper mapper = new DefaultConfiguration().jacksonObjectMapper();

    @Test
    public void read() throws IOException {
        String json = Helper.getResourceAsString("/net/iteach/service/admin/comment.json");
        ExportedComment comment = mapper.readValue(json, ExportedComment.class);
        assertNotNull(comment);
        assertEquals(new DateTime(2013, 2, 13, 14, 27, 11, DateTimeZone.UTC), comment.getCreation());
        assertNull(comment.getEdition());
        assertEquals("Some comments", comment.getContent());
    }

}
