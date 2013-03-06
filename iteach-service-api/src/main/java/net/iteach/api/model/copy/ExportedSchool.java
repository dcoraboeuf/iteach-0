package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Coordinate;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
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

    @JsonCreator
    public ExportedSchool(
            @JsonProperty("comments")
            List<ExportedComment> comments,
            @JsonProperty("coordinates")
            List<Coordinate> coordinates,
            @JsonProperty("name")
            String name,
            @JsonProperty("color")
            String color,
            @JsonProperty("hrate")
            BigDecimal hrate,
            @JsonProperty("currency")
            String currency,
            @JsonProperty("students")
            List<ExportedStudent> students) {
        super(comments, coordinates);
        this.name = name;
        this.color = color;
        this.hrate = hrate;
        this.currency = currency;
        this.students = students;
    }

    public ExportedSchool(
            List<ExportedComment> comments,
            List<Coordinate> coordinates,
            String name,
            String color,
            Money hrate,
            List<ExportedStudent> students) {
        this(comments, coordinates, name, color, hrate.getAmount(), hrate.getCurrencyUnit().getCurrencyCode(), students);
    }
}
