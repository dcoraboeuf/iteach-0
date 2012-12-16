package net.iteach.web.ui;

import net.iteach.api.SchoolService;
import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.ui.TeacherUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ui/teacher")
public class TeacherUIController implements TeacherUI {

	private final SchoolService schoolService;

	@Autowired
	public TeacherUIController(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	@Override
	@RequestMapping(value = "/school", method = RequestMethod.GET)
	public @ResponseBody
	SchoolSummaries getSchools() {
		// FIXME Gets the current teacher
		// OK
		return schoolService.getSchoolsForTeacher(0);
	}

	@Override
	@RequestMapping(value = "/school", method = RequestMethod.POST)
	public @ResponseBody
	ID createSchool(@RequestBody SchoolForm form) {
		// FIXME Gets the current teacher
		// OK
		return schoolService.createSchoolForTeacher(0, form);
	}

}
