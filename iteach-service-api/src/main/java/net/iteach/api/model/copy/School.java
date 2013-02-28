package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.money.Money;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class School extends WithCoordinates {

    private final String name;
    private final String color;
    private final Money hrate;
    private final List<Student> students;

    public School(List<Comment> comments, List<CoordinateCopy> coordinates, String name, String color, Money hrate, List<Student> students) {
        super(comments, coordinates);
        this.name = name;
        this.color = color;
        this.hrate = hrate;
        this.students = students;
    }
}
