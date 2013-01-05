package net.iteach.core.model;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class CommentSummary {
	
	private final int id;
	private final DateTime creation;
	private final DateTime edition;
	private final CommentFormat format;
	private final String content;
	private final boolean summary;

}
