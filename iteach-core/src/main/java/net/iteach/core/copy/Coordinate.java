package net.iteach.core.copy;

import lombok.Data;
import net.iteach.core.model.CoordinateType;

@Data
public class Coordinate {

    private final CoordinateType type;
    private final String value;

}
