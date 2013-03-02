package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Comment;
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

    public ExportedLesson(List<Comment> comments, LocalDate date, LocalTime from, LocalTime to, String location) {
        super(comments);
        this.date = date;
        this.from = from;
        this.to = to;
        this.location = location;
    }
}
