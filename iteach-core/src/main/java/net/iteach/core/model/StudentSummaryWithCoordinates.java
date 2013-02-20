package net.iteach.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentSummaryWithCoordinates extends StudentSummary {

	private final Coordinates coordinates;

	public StudentSummaryWithCoordinates(int id, String subject, String name,
			SchoolSummaryWithCoordinates school, boolean disabled, Coordinates coordinates) {
		super(id, subject, name, school, disabled);
		this.coordinates = coordinates;
	}

}
