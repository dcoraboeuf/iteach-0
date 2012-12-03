package net.iteach.web.home;

import net.iteach.core.ui.TeacherUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/home")
public class HomeController {

	private final TeacherUI ui;

	@Autowired
	public HomeController(TeacherUI ui) {
		this.ui = ui;
	}

	/**
	 * Home page
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(Model model) {
		// List of schools
		model.addAttribute("schools", ui.getSchools());
		// OK
		return "home";
	}

}
