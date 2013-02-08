package net.iteach.web.planning;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.iteach.api.PreferenceService;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Lesson;
import net.iteach.core.model.LessonListRequest;
import net.iteach.core.model.Lessons;
import net.iteach.core.security.SecurityUtils;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.AbstractUIController;
import net.iteach.web.support.ErrorHandler;
import net.iteach.web.support.UserSession;
import net.iteach.web.ui.LessonEvent;
import net.iteach.web.ui.LessonEvents;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/gui/lesson")
public class PlanningController extends AbstractUIController {

	private final TeacherUI teacherUI;
	private final UserSession userSession;
    private final PreferenceService preferenceService;

	@Autowired
	public PlanningController(SecurityUtils securityUtils,
                              ErrorHandler errorHandler, Strings strings, TeacherUI teacherUI, UserSession userSession, PreferenceService preferenceService) {
		super(securityUtils, errorHandler, strings);
		this.teacherUI = teacherUI;
		this.userSession = userSession;
        this.preferenceService = preferenceService;
    }

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public @ResponseBody LessonEvents lessons(@RequestBody LessonListRequest request, HttpSession session) {
		// Gets the regular lessons from the model
		Lessons lessons = teacherUI.getLessons(request.getRange());
		// Transformation
		List<LessonEvent> events = Lists.transform(lessons.getLessons(), new Function<Lesson, LessonEvent>() {
			@Override
			public LessonEvent apply (Lesson lesson) {
				return toEvent (lesson);
			}
		});
		// Sets the current date
		if (request.isSetDate()) {
			userSession.setCurrentDate(session, request.getRange().getFrom().toLocalDate());
		}
		// OK
		return new LessonEvents(events);
	}

    @RequestMapping(value = "/bound/{bound}/{direction}", method = RequestMethod.POST)
    public @ResponseBody
    Ack planningRange (@PathVariable PlanningBound bound, @PathVariable PlanningBoundDirection direction) {
        // Gets the current value, as integer
        int value = preferenceService.getPreferenceAsInt(bound.getPreferenceKey());
        // Increase/decreases the value
        int newValue = direction.change(value);
        // Sets the value as an int
        preferenceService.setPreference(bound.getPreferenceKey(), newValue);
        // OK
        return Ack.OK;
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
