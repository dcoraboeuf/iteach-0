package net.iteach.api;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.SchoolForm;
import net.iteach.core.model.SchoolSummaries;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface SchoolService {

	SchoolSummaries getSchoolsForTeacher(int teacherId);

	ID createSchoolForTeacher(int teacherId, @Valid SchoolForm form);

	Ack deleteSchoolForTeacher(int teacherId, int id);

	Ack editSchoolForTeacher(int userId, int id, SchoolForm form);

}
