package net.iteach.core.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Container for a list of coordinates.
 */
@Data
public class Coordinates {

    private final Map<String,Coordinate> coordinates;

}
