package net.iteach.core.model;

import lombok.Data;
import net.iteach.core.validation.SchoolFormValidation;

@Data
public class SchoolForm implements SchoolFormValidation {

	private final String name;
	private final String color;

}
