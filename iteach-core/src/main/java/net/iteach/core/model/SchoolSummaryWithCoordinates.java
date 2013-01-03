package net.iteach.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SchoolSummaryWithCoordinates extends SchoolSummary {

	private final Coordinates coordinates;

	public SchoolSummaryWithCoordinates(int id, String name, String color,
			Coordinates coordinates) {
		super(id, name, color);
		this.coordinates = coordinates;
	}

}
