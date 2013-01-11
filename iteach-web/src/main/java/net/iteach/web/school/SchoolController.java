package net.iteach.web.school;

import net.iteach.core.ui.TeacherUI;
import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gui/school")
public class SchoolController extends AbstractGUIController {

	private final TeacherUI ui;
	
	@Autowired
	public SchoolController(ErrorHandler errorHandler, TeacherUI ui) {
		super(errorHandler);
		this.ui = ui;
	}

	@RequestMapping("/{id:\\d+}")
	public String details (@PathVariable int id, Model model) {
		// Loads the school details
		model.addAttribute("school", ui.getSchool(id));
		// View
		return "school";
	}

}
