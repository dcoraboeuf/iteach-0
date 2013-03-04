package net.iteach.service.dao;

import net.iteach.api.model.CoordinateEntity;
import net.iteach.core.model.CoordinateType;
import net.iteach.service.dao.model.TCoordinate;

import java.util.List;

public interface CoordinateDao {

    void deleteCoordinate(CoordinateEntity entity, int id, CoordinateType type);

    void storeCoordinate(CoordinateEntity entity, int id, CoordinateType type, String value);

    List<TCoordinate> getCoordinates(CoordinateEntity entity, int id);

}
