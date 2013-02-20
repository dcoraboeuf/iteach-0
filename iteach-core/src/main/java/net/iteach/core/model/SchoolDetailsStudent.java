package net.iteach.core.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SchoolDetailsStudent {

	private final int id;
	private final String name;
	private final String subject;
    private final boolean disabled;
	private final BigDecimal hours;
	
}
