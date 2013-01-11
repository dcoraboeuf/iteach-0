package net.iteach.web.planning;

import javax.servlet.http.HttpSession;

import net.iteach.core.model.LessonDetails;
import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;
import net.iteach.web.support.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/lesson")
public class LessonController extends AbstractGUIController {

	private final TeacherUI teacherUI;
	private final UserSession userSession;

	@Autowired
	public LessonController(ErrorHandler errorHandler, TeacherUI teacherUI, UserSession userSession) {
		super(errorHandler);
		this.teacherUI = teacherUI;
		this.userSession = userSession;
	}

	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
	public String lesson (@PathVariable int id, Model model, HttpSession session) {
		// Loads the lesson
		LessonDetails lesson = teacherUI.getLesson(id);
		// Sets the lesson for the page
		model.addAttribute("lesson", lesson);
		// Adjust the current date
		userSession.setCurrentDate(session, lesson.getDate());
		// OK
		return "lesson";
	}

}
