package net.iteach.api;

import net.iteach.api.model.Entity;
import net.iteach.core.model.Coordinates;

public interface CoordinatesService {

	void setCoordinates(Entity entity, int id, Coordinates coordinates);

	void removeCoordinates(Entity entity, int id);

	Coordinates getCoordinates(Entity entity, int id);

}
