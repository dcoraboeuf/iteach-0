package net.iteach.core.model;

import lombok.Data;
import net.iteach.core.validation.SchoolValidation;

@Data
public class SchoolForm implements SchoolValidation {

	private final String name;
	private final String color;

}
