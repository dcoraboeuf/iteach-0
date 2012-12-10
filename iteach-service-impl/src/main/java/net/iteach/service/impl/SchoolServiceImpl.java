package net.iteach.service.impl;

import java.util.Collections;

import javax.sql.DataSource;

import net.iteach.api.SchoolService;
import net.iteach.core.model.SchoolSummaries;
import net.iteach.core.model.SchoolSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchoolServiceImpl extends AbstractServiceImpl implements
		SchoolService {

	@Autowired
	public SchoolServiceImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	@Transactional(readOnly = true)
	public SchoolSummaries getSchoolsForTeacher(int teacherId) {
		// FIXME SchoolServiceImpl.getSchoolsForTeacher
		return new SchoolSummaries(Collections.<SchoolSummary>emptyList());
//		return new SchoolSummaries(
//				getNamedParameterJdbcTemplate().query(SQL.SCHOOLS_FOR_TEACHER, params("teacher", teacherId),
//						new RowMapper<SchoolSummary>() {
//							@Override
//							public SchoolSummary mapRow(ResultSet rs, int rowNum)
//									throws SQLException {
//								return new SchoolSummary(rs.getInt("id"), rs.getString("name"));
//							}
//						}));
	}

}
