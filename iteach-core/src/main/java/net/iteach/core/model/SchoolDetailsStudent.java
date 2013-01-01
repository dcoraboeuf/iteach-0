package net.iteach.core.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SchoolDetailsStudent {

	private final int id;
	private final String name;
	private final String subject;
	private final BigDecimal hours;
	
}
