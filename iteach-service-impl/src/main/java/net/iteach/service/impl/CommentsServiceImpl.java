package net.iteach.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.iteach.api.CommentsService;
import net.iteach.api.model.Entity;
import net.iteach.core.model.Comment;
import net.iteach.core.model.Comments;
import net.iteach.service.db.SQLUtils;

@Service
public class CommentsServiceImpl extends AbstractServiceImpl implements CommentsService {
	
	private static final String SQL_SELECT_FOR_ENTITY_WITH_OFFSET = "SELECT ID, CREATION, EDITION, CONTENT FROM COMMENTS WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id";

	@Autowired
	public CommentsServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional(readOnly = true)
	public Comments getComments(Entity entity, int id, int offset, int count) {
		// FIXME Offset and count
		return new Comments(
			getNamedParameterJdbcTemplate().query(
				String.format(SQL_SELECT_FOR_ENTITY_WITH_OFFSET, entity),
				params("id", id),
				new RowMapper<Comment>() {
					@Override
					public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Comment(
							rs.getInt("id"),
							SQLUtils.getDateTime(rs, "creation"),
							SQLUtils.getDateTime(rs, "edition"),
							rs.getString("content")
							);
					}
				}
			)
		);
	}

}
