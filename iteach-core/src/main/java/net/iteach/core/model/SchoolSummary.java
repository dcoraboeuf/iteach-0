package net.iteach.core.model;

import org.joda.money.Money;

import lombok.Data;

@Data
public class SchoolSummary {
	
	private final int id;
	private final String name;
	private final String color;
	private final Money hourlyRate;

}
