package net.iteach.service.dao;

import net.iteach.service.dao.model.TStudent;

import java.util.List;

public interface StudentDao {
    List<TStudent> findStudentsByTeacher(int teacherId);

    TStudent getStudentById(int studentId);
}
