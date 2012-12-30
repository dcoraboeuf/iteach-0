package net.iteach.web.student;

import javax.servlet.http.HttpSession;

import net.iteach.core.ui.TeacherUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gui/student")
public class StudentController {

	private final TeacherUI ui;
	
	@Autowired
	public StudentController(TeacherUI ui) {
		this.ui = ui;
	}

	@RequestMapping("/{id:\\d+}")
	public String details (@PathVariable int id, Model model, HttpSession session) {
		// Loads the student details
		model.addAttribute("student", ui.getStudent(id));
		// View
		return "student";
	}

}
