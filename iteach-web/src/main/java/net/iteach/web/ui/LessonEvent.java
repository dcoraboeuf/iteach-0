package net.iteach.web.ui;

import net.iteach.core.model.Lesson;
import lombok.Data;

@Data
public class LessonEvent {
	
	private final int id;
	private final String title;
	private final String start;
	private final String end;
	private final String backgroundColor;
	private final Lesson lesson;
	
	public boolean isAllDay () {
		return false;
	}

}
