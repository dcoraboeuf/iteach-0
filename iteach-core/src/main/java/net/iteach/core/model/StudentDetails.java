package net.iteach.core.model;

import lombok.Data;

@Data
public class StudentDetails {
	
	private final int id;
	private final String subject;
	private final String name;
	private final SchoolSummary school;

}
