package net.iteach.service.impl;

import static java.lang.String.format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.CoordinatesService;
import net.iteach.api.model.Entity;
import net.iteach.core.model.CoordinateType;
import net.iteach.core.model.Coordinates;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoordinatesServiceImpl extends AbstractServiceImpl implements CoordinatesService {

	private static final String SQL_DELETE = "DELETE FROM COORDINATES WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id";
	
	private static final String SQL_DELETE_FOR_TYPE = "DELETE FROM COORDINATES WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id AND COORD_TYPE = :type";
	private static final String SQL_SELECT_FOR_TYPE = "SELECT COORD_VALUE FROM COORDINATES WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id AND COORD_TYPE = :type";
	private static final String SQL_UPDATE_FOR_TYPE = "UPDATE COORDINATES SET COORD_VALUE = :value WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id AND COORD_TYPE = :type";
	private static final String SQL_INSERT_FOR_TYPE = "INSERT INTO COORDINATES (ENTITY_TYPE, ENTITY_ID, COORD_TYPE, COORD_VALUE) VALUES ('%s', :id, :type, :value)";
	

	private static final String SQL_SELECT_FOR_ENTITY = "SELECT COORD_TYPE, COORD_VALUE FROM COORDINATES WHERE ENTITY_TYPE = '%s' AND ENTITY_ID = :id";

	@Autowired
	public CoordinatesServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	@Transactional
	public void setCoordinates(Entity entity, int id, Coordinates coordinates) {
		for (Map.Entry<CoordinateType, String> entry: coordinates.entries()) {
			CoordinateType type = entry.getKey();
			String value = entry.getValue();
			MapSqlParameterSource params = params(id, type);
			if (StringUtils.isBlank(value)) {
				getNamedParameterJdbcTemplate().update(format(SQL_DELETE_FOR_TYPE, entity), params);
			} else {
				String existingValue = getFirstItem(format(SQL_SELECT_FOR_TYPE, entity), params, String.class);
				params = params.addValue("value", value);
				if (existingValue == null) {
					getNamedParameterJdbcTemplate().update(format(SQL_INSERT_FOR_TYPE, entity), params);
				} else {
					getNamedParameterJdbcTemplate().update(format(SQL_UPDATE_FOR_TYPE, entity), params);
				}
			}
		}
	}

	protected MapSqlParameterSource params(int id, CoordinateType type) {
		return params("id", id).addValue("type", type.name());
	}

	@Override
	@Transactional
	public void removeCoordinates(Entity entity, int id) {
		String sql = format(SQL_DELETE, entity);
		getNamedParameterJdbcTemplate().update(
			sql,
			params("id", id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Coordinates getCoordinates(Entity entity, int id) {
		final AtomicReference<Coordinates> coordinates = new AtomicReference<>(Coordinates.create());
		getNamedParameterJdbcTemplate().query(
			format(SQL_SELECT_FOR_ENTITY, entity),
			params("id", id),
			new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					CoordinateType type = CoordinateType.valueOf(rs.getString("coord_type"));
					String value = rs.getString("coord_value");
					coordinates.set(coordinates.get().add(type, value));
				}
			});
		return coordinates.get();
	}

}
