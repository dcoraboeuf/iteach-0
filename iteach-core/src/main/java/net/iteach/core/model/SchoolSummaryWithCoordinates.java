package net.iteach.core.model;

import org.joda.money.Money;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SchoolSummaryWithCoordinates extends SchoolSummary {

	private final Coordinates coordinates;

	public SchoolSummaryWithCoordinates(int id, String name, String color, Money hourlyRate,
			Coordinates coordinates) {
		super(id, name, color, hourlyRate);
		this.coordinates = coordinates;
	}

}
