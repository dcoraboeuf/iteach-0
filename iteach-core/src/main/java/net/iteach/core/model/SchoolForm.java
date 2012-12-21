package net.iteach.core.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SchoolForm {

    @NotNull
    @Size(min=1, max=80)
	private final String name;

    @NotNull
    @Size(min=7, max=7)
	private final String color;

}
