package net.iteach.core.model;

import lombok.Data;

@Data
public class LessonListRequest {
	
	private final LessonRange range;
	private final boolean setDate;

}
