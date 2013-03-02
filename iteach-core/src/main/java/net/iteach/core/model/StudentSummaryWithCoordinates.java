package net.iteach.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSummaryWithCoordinates extends StudentSummary {

    private final Coordinates coordinates;

    public StudentSummaryWithCoordinates(StudentSummary studentSummary,
                                         SchoolSummaryWithCoordinates school, Coordinates coordinates) {
        super(
                studentSummary.getId(),
                studentSummary.getSubject(),
                studentSummary.getName(),
                school,
                studentSummary.isDisabled());
        this.coordinates = coordinates;
    }

}
