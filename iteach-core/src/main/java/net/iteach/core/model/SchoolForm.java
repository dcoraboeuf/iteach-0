package net.iteach.core.model;

import java.math.BigDecimal;

import lombok.Data;
import net.iteach.core.validation.SchoolFormValidation;

@Data
public class SchoolForm implements SchoolFormValidation {

	private final String name;
	private final String color;
	private final BigDecimal hourlyRate;
    private final Coordinates coordinates;

}
