package net.iteach.web.home;

import javax.servlet.http.HttpSession;

import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;
import net.iteach.web.support.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/home")
public class HomeController extends AbstractGUIController {

	private final TeacherUI ui;
	private final UserSession userSession;

	@Autowired
	public HomeController(ErrorHandler errorHandler, TeacherUI ui, UserSession userSession) {
		super(errorHandler);
		this.ui = ui;
		this.userSession = userSession;
	}

	/**
	 * Home page
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(Model model, HttpSession session) {
		// List of students
		model.addAttribute("students", ui.getStudents());
		// List of schools
		model.addAttribute("schools", ui.getSchools());
		// Current date
		model.addAttribute("currentDate", userSession.getCurrentDate(session).toString());
		// OK
		return "home";
	}

}
