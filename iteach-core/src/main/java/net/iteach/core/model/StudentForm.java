package net.iteach.core.model;

import lombok.Data;
import net.iteach.core.validation.StudentFormValidation;

@Data
public class StudentForm implements StudentFormValidation {

	private final int school;
	private final String subject;
	private final String name;
    private final Coordinates coordinates;

}
