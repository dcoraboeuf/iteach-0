package net.iteach.core.model;

import org.joda.money.Money;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolSummaryWithCoordinates extends SchoolSummary {

    private final Coordinates coordinates;

    public SchoolSummaryWithCoordinates(SchoolSummary schoolSummary,
                                        Coordinates coordinates) {
        super(
                schoolSummary.getId(),
                schoolSummary.getName(),
                schoolSummary.getColor(),
                schoolSummary.getHourlyRate());
        this.coordinates = coordinates;
    }

}
