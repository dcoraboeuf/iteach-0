package net.iteach.api;

import net.iteach.api.model.CoordinateEntity;
import net.iteach.core.model.Coordinates;

public interface CoordinatesService {

	void setCoordinates(CoordinateEntity entity, int id, Coordinates coordinates);

	Coordinates getCoordinates(CoordinateEntity entity, int id);

}
