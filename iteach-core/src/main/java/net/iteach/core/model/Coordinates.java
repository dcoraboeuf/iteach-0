package net.iteach.core.model;

import lombok.Data;

import java.util.List;

/**
 * Container for a list of coordinates.
 */
@Data
public class Coordinates {

    private final List<Coordinate> coordinates;

}
