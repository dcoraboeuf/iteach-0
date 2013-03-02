package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Comment;
import net.iteach.core.model.Coordinate;
import org.joda.money.Money;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportedSchool extends ExportedWithCoordinates {

    private final String name;
    private final String color;
    private final Money hrate;
    private final List<ExportedStudent> students;

    public ExportedSchool(List<Comment> comments, List<Coordinate> coordinates, String name, String color, Money hrate, List<ExportedStudent> students) {
        super(comments, coordinates);
        this.name = name;
        this.color = color;
        this.hrate = hrate;
        this.students = students;
    }
}
