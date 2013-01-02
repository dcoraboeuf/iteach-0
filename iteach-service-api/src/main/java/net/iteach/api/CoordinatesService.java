package net.iteach.api;

import net.iteach.api.model.CoordinatesEntity;
import net.iteach.core.model.Coordinates;

public interface CoordinatesService {

	void setCoordinates(CoordinatesEntity entity, int id, Coordinates coordinates);

	void removeCoordinates(CoordinatesEntity entity, int id);

}
