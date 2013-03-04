package net.iteach.service.dao.model;

import lombok.Data;
import net.iteach.core.model.CoordinateType;

@Data
public class TCoordinate {

    private final CoordinateType type;
    private final String value;

}
