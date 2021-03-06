package net.iteach.core.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Data;

@Data
public class LessonDetails {
	
	private final int id;
	private final StudentSummaryWithCoordinates student;
	private final LocalDate date;
	private final LocalTime from;
	private final LocalTime to;
	private final String location;

}
