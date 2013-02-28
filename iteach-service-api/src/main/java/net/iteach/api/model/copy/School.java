package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class School extends WithCoordinates {

    private final String name;
    private final String color;
    private final BigDecimal hrate;
    private final List<Student> students;

    public School(List<Comment> comments, List<Coordinate> coordinates, String name, String color, BigDecimal hrate, List<Student> students) {
        super(comments, coordinates);
        this.name = name;
        this.color = color;
        this.hrate = hrate;
        this.students = students;
    }
}
