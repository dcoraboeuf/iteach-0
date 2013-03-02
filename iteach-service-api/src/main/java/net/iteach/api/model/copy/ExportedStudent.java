package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Comment;
import net.iteach.core.model.Coordinate;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportedStudent extends ExportedWithCoordinates {

    private final String name;
    private final String subject;
    private final boolean disabled;
    private final List<ExportedLesson> lessons;

    public ExportedStudent(List<Comment> comments, List<Coordinate> coordinates, String name, String subject, boolean disabled, List<ExportedLesson> lessons) {
        super(comments, coordinates);
        this.name = name;
        this.subject = subject;
        this.disabled = disabled;
        this.lessons = lessons;
    }
}
