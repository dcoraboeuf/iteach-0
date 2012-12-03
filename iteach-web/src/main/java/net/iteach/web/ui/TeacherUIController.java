package net.iteach.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.iteach.api.SchoolService;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.ui.TeacherUI;

@Controller
@RequestMapping("/ui/teacher")
public class TeacherUIController implements TeacherUI {

	private final SchoolService schoolService;

	@Autowired
	public TeacherUIController(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	@Override
	@RequestMapping(value = "/schools", method = RequestMethod.GET)
	public @ResponseBody
	SchoolSummaries getSchools() {
		// FIXME Gets the current teacher
		// OK
		return schoolService.getSchoolsForTeacher(0);
	}

}
