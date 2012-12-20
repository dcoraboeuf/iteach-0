package net.iteach.core.model;

import lombok.Data;

@Data
public class StudentSummary {
	
	private final int id;
	private final String subject;
	private final String firstName;
	private final String lastName;
	private final SchoolSummary school;

}
