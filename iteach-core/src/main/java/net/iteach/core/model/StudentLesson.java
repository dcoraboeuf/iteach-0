package net.iteach.core.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Data;

@Data
public class StudentLesson {
	
	private final int id;
	private final LocalDate date;
	private final LocalTime from;
	private final LocalTime to;
	private final String location;
    private final String localizedDate;
    private final String localizedFrom;
    private final String localizedTo;

}
