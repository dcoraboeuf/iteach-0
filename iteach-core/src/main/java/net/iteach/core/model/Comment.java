package net.iteach.core.model;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class Comment {
	
	private final int id;
	private final DateTime creation;
	private final DateTime edition;
	private final String content;
	
}
