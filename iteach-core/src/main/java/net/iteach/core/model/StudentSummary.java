package net.iteach.core.model;

import lombok.Data;

@Data
public class StudentSummary {
	
	private final int id;
	private final String subject;
	private final String name;
	private final SchoolSummary school;
    private final boolean disabled;

}
