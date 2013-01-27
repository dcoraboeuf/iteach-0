package net.iteach.core.model;

import java.math.BigDecimal;
import java.util.List;

import org.joda.money.Money;

import lombok.Data;

@Data
public class SchoolDetails {
	
	private final int id;
	private final String name;
	private final String color;
	private final Money hourlyRate;
	private final Coordinates coordinates;
	private final List<SchoolDetailsStudent> students;
	private final BigDecimal totalHours;

}
