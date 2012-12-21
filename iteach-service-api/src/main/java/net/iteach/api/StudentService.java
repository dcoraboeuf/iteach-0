package net.iteach.api;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.core.model.StudentForm;
import net.iteach.core.model.StudentSummaries;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface StudentService {

	StudentSummaries getStudentsForTeacher(int teacherId);

	ID createStudentForTeacher(int teacherId, @Valid StudentForm form);

	Ack deleteStudentForTeacher(int teacherId, int id);

	Ack editStudentForTeacher(int userId, int id, @Valid StudentForm form);

}
