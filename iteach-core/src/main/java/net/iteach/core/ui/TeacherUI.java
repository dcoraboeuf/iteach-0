package net.iteach.core.ui;

import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;

public interface TeacherUI {

	SchoolSummaries getSchools();
	
	ID createSchool (SchoolForm form);

}
