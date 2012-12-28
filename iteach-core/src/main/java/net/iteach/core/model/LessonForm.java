package net.iteach.core.model;

import net.iteach.core.validation.LessonFormValidation;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Data;

@Data
public class LessonForm implements LessonFormValidation {
	
	private final LocalDate date;
	private final LocalTime from;
	private final LocalTime to;
	private final int student;
	private final String location;

}
