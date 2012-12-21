package net.iteach.core.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class StudentForm {

	private final int school;

    @NotNull
    @Size(max = 80)
	private final String subject;

    @NotNull
    @Size(max = 40)
	private final String firstName;

    @NotNull
    @Size(min = 1, max = 40)
	private final String lastName;

}
