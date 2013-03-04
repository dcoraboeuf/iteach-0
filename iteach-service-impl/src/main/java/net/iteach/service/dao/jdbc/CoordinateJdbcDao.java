package net.iteach.service.dao.jdbc;

import net.iteach.api.model.CoordinateEntity;
import net.iteach.core.model.CoordinateType;
import net.iteach.service.dao.CoordinateDao;
import net.iteach.service.dao.model.TCoordinate;
import net.iteach.service.db.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;

@Component
public class CoordinateJdbcDao extends AbstractJdbcDao implements CoordinateDao {

    private static final String SQL_DELETE_FOR_TYPE = "DELETE FROM COORDINATES WHERE %s = :id AND COORD_TYPE = :type";
    private static final String SQL_SELECT_FOR_TYPE = "SELECT COORD_VALUE FROM COORDINATES WHERE %s = :id AND COORD_TYPE = :type";
    private static final String SQL_UPDATE_FOR_TYPE = "UPDATE COORDINATES SET COORD_VALUE = :value WHERE %s = :id AND COORD_TYPE = :type";
    private static final String SQL_INSERT_FOR_TYPE = "INSERT INTO COORDINATES (%s, COORD_TYPE, COORD_VALUE) VALUES (:id, :type, :value)";
    private static final String SQL_SELECT_FOR_ENTITY = "SELECT COORD_TYPE, COORD_VALUE FROM COORDINATES WHERE %s = :id";

    @Autowired
    public CoordinateJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Transactional
    public void deleteCoordinate(CoordinateEntity entity, int id, CoordinateType type) {
        getNamedParameterJdbcTemplate().update(
                format(SQL_DELETE_FOR_TYPE, entity.name()),
                params("id", id).addValue("type", type.name()));
    }

    @Override
    @Transactional
    public void storeCoordinate(CoordinateEntity entity, int id, CoordinateType type, String value) {
        MapSqlParameterSource params = params("id", id)
                .addValue("type", type.name())
                .addValue("value", value);
        String existingValue = getFirstItem(
                format(SQL_SELECT_FOR_TYPE, entity.name()),
                params, String.class);
        if (existingValue == null) {
            getNamedParameterJdbcTemplate().update(format(SQL_INSERT_FOR_TYPE, entity.name()), params);
        } else {
            getNamedParameterJdbcTemplate().update(format(SQL_UPDATE_FOR_TYPE, entity.name()), params);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TCoordinate> getCoordinates(CoordinateEntity entity, int id) {
        MapSqlParameterSource params = params("id", id);
        return getNamedParameterJdbcTemplate().query(
                format(SQL_SELECT_FOR_ENTITY, entity.name()),
                params,
                new RowMapper<TCoordinate>() {
                    @Override
                    public TCoordinate mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new TCoordinate(
                                SQLUtils.getEnum(CoordinateType.class, rs, "coord_type"),
                                rs.getString("coord_value")
                        );
                    }
                }
        );
    }
}
