package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Coordinate;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportedSchool extends ExportedWithCoordinates {

    private final String name;
    private final String color;
    private final BigDecimal hrate;
    private final String currency;
    private final List<ExportedStudent> students;

    public ExportedSchool(List<ExportedComment> comments, List<Coordinate> coordinates, String name, String color, Money hrate, List<ExportedStudent> students) {
        super(comments, coordinates);
        this.name = name;
        this.color = color;
        this.hrate = hrate.getAmount();
        this.currency = hrate.getCurrencyUnit().getCurrencyCode();
        this.students = students;
    }
}
