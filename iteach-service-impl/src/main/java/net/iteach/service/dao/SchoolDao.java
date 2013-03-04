package net.iteach.service.dao;

import net.iteach.core.model.Ack;
import net.iteach.core.model.ID;
import net.iteach.service.dao.model.TSchool;

import java.math.BigDecimal;
import java.util.List;

public interface SchoolDao {

    List<TSchool> findSchoolsByTeacher(int teacherId);

    TSchool getSchoolById(int id);

    ID createSchool(int teacherId, String name, String color, BigDecimal hourlyRate);

    Ack deleteSchool(int id);

    Ack updateSchool(int id, String name, String color, BigDecimal hourlyRate);

    boolean doesSchoolBelongToTeacher(int id, int userId);
}
