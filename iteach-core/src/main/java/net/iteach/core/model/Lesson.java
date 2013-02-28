package net.iteach.core.model;

import net.iteach.core.model.StudentSummary;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Data;

@Data
public class Lesson {
	
	private final int id;
	private final StudentSummary student;
	private final LocalDate date;
	private final LocalTime from;
	private final LocalTime to;
	private final String location;

}
