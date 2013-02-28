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
    private final List<Lesson> lessons;

    public Student(List<Comment> comments, List<Coordinate> coordinates, String name, String subject, boolean disabled, List<Lesson> lessons) {
        super(comments, coordinates);
        this.name = name;
        this.subject = subject;
        this.disabled = disabled;
        this.lessons = lessons;
    }
}
