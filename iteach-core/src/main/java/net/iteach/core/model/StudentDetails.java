package net.iteach.core.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentDetails {
	
	private final int id;
	private final String subject;
	private final String name;
	private final Coordinates coordinates;
	private final SchoolSummary school;
	private final BigDecimal totalHours;
    private final boolean disabled;

}
