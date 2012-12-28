package net.iteach.core.model;

import lombok.Data;

import org.joda.time.LocalDateTime;

@Data
public class LessonRange {

	private final LocalDateTime from;
	private final LocalDateTime to;

}
