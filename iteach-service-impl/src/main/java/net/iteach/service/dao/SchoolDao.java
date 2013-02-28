package net.iteach.service.dao;

import net.iteach.service.dao.model.TSchool;

import java.util.List;

public interface SchoolDao {

    List<TSchool> findSchoolsByTeacher(int teacherId);

}
