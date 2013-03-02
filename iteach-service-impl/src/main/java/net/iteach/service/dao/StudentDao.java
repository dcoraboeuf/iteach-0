package net.iteach.service.dao;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.service.dao.model.TStudent;

import java.util.List;

public interface StudentDao {
    List<TStudent> findStudentsByTeacher(int teacherId);

    TStudent getStudentById(int studentId);

    List<TStudent> findStudentsBySchool(int schoolId);

    ID createStudent(String name, int school, String subject);

    Ack deleteStudent(int id);

    Ack disableStudent(int id);

    Ack enableStudent(int id);

    Ack updateStudent(int id, String name, int school, String subject);
}
