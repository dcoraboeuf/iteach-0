package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportedLesson extends ExportedWithComments {

    private final LocalDate date;
    private final LocalTime from;
    private final LocalTime to;
    private final String location;

    @JsonCreator
    public ExportedLesson(
            @JsonProperty("comments")
            List<ExportedComment> comments,
            @JsonProperty("date")
            LocalDate date,
            @JsonProperty("from")
            LocalTime from,
            @JsonProperty("to")
            LocalTime to,
            @JsonProperty("location")
            String location) {
        super(comments);
        this.date = date;
        this.from = from;
        this.to = to;
        this.location = location;
    }
}
