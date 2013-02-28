package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Student extends WithCoordinates {

    private final String name;
    private final String subject;
    private final boolean disabled;
    private final List<LessonCopy> lessons;

    public Student(List<Comment> comments, List<CoordinateCopy> coordinates, String name, String subject, boolean disabled, List<LessonCopy> lessons) {
        super(comments, coordinates);
        this.name = name;
        this.subject = subject;
        this.disabled = disabled;
        this.lessons = lessons;
    }
}
