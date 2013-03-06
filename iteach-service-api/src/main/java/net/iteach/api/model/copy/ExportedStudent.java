package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Coordinate;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportedStudent extends ExportedWithCoordinates {

    private final String name;
    private final String subject;
    private final boolean disabled;
    private final List<ExportedLesson> lessons;

    @JsonCreator
    public ExportedStudent(
            @JsonProperty("comments")
            List<ExportedComment> comments,
            @JsonProperty("coordinates")
            List<Coordinate> coordinates,
            @JsonProperty("name")
            String name,
            @JsonProperty("subject")
            String subject,
            @JsonProperty("disabled")
            boolean disabled,
            @JsonProperty("lessons")
            List<ExportedLesson> lessons) {
        super(comments, coordinates);
        this.name = name;
        this.subject = subject;
        this.disabled = disabled;
        this.lessons = lessons;
    }
}
