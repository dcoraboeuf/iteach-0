package net.iteach.service.impl;

import net.iteach.api.CoordinatesService;
import net.iteach.api.model.CoordinateEntity;
import net.iteach.core.model.CoordinateType;
import net.iteach.core.model.Coordinates;
import net.iteach.service.dao.CoordinateDao;
import net.iteach.service.dao.model.TCoordinate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CoordinatesServiceImpl implements CoordinatesService {

    private final CoordinateDao coordinateDao;

    @Autowired
    public CoordinatesServiceImpl(CoordinateDao coordinateDao) {
        this.coordinateDao = coordinateDao;
    }

    @Override
    @Transactional
    public void setCoordinates(CoordinateEntity entity, int id, Coordinates coordinates) {
        for (Map.Entry<CoordinateType, String> entry : coordinates.entries()) {
            CoordinateType type = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                coordinateDao.deleteCoordinate(entity, id, type);
            } else {
                coordinateDao.storeCoordinate(entity, id, type, value);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Coordinates getCoordinates(CoordinateEntity entity, int id) {
        List<TCoordinate> tCoordinates = coordinateDao.getCoordinates(entity, id);
        Coordinates coordinates = Coordinates.create();
        for (TCoordinate tCoordinate : tCoordinates) {
            coordinates = coordinates.add(tCoordinate.getType(), tCoordinate.getValue());
        }
        return coordinates;
    }

}
