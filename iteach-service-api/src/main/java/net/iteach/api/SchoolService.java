package net.iteach.api;

import net.iteach.core.model.SchoolSummaries;

public interface SchoolService {

	SchoolSummaries getSchoolsForTeacher(int teacherId);

}
