package net.iteach.web.planning;

import java.util.List;

import net.iteach.core.model.Lesson;
import net.iteach.core.model.LessonRange;
import net.iteach.core.model.Lessons;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.ErrorHandler;
import net.iteach.web.ui.AbstractUIController;
import net.iteach.web.ui.LessonEvent;
import net.iteach.web.ui.LessonEvents;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/gui/lesson")
public class PlanningController extends AbstractUIController {

	private final TeacherUI teacherUI;

	@Autowired
	public PlanningController(SecurityUtils securityUtils,
			ErrorHandler errorHandler, Strings strings, TeacherUI teacherUI) {
		super(securityUtils, errorHandler, strings);
		this.teacherUI = teacherUI;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
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

	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
	public String lesson (@PathVariable int id, Model model) {
		// Loads the lesson
		model.addAttribute("lesson", teacherUI.getLesson(id));
		// OK
		return "lesson";
	}

	protected LessonEvent toEvent(Lesson lesson) {
		String start = lesson.getDate().toLocalDateTime(lesson.getFrom()).toString();
		String end = lesson.getDate().toLocalDateTime(lesson.getTo()).toString();
		String title = getLessonTitle(lesson);
		return new LessonEvent(
				lesson.getId(),
				title,
				start,
				end,
				lesson.getStudent().getSchool().getColor(),
				lesson);
	}

	protected String getLessonTitle(Lesson lesson) {
		String location = lesson.getLocation();
		String studentName = lesson.getStudent().getName();
		if (StringUtils.isBlank(location)) {
			return studentName;
		} else {
			return String.format("%s @ %s", studentName, location);
		}
	}

}
