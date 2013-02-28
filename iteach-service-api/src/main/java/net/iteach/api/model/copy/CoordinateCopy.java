package net.iteach.api.model.copy;

import lombok.Data;
import net.iteach.core.model.CoordinateType;

@Data
public class CoordinateCopy {

    private final CoordinateType type;
    private final String value;

}
