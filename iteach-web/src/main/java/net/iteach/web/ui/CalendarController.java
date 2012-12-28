package net.iteach.web.ui;

import java.util.List;

import net.iteach.core.model.Lesson;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/ui/calendar")
public class CalendarController extends AbstractUIController {

	private final TeacherUI teacherUI;

	@Autowired
	public CalendarController(SecurityUtils securityUtils,
			ErrorHandler errorHandler, Strings strings, TeacherUI teacherUI) {
		super(securityUtils, errorHandler, strings);
		this.teacherUI = teacherUI;
	}

	@RequestMapping("/lessons")
	public @ResponseBody LessonEvents lessons(@RequestBody LessonRange range) {
		// Gets the regular lessons from the model
		Lessons lessons = teacherUI.getLessons(range);
		// Transformation
		List<LessonEvent> events = Lists.transform(lessons.getLessons(), new Function<Lesson, LessonEvent>() {
			@Override
			public LessonEvent apply (Lesson lesson) {
				return toEvent (lesson);
			}
		});
		// OK
		return new LessonEvents(events);
	}

	protected LessonEvent toEvent(Lesson lesson) {
		String start = lesson.getDate().toLocalDateTime(lesson.getFrom()).toString();
		String end = lesson.getDate().toLocalDateTime(lesson.getTo()).toString();
		return new LessonEvent(
				lesson.getId(),
				lesson.getStudent().getName(),
				start,
				end,
				lesson.getStudent().getSchool().getColor(),
				lesson);
	}

}
