package net.iteach.core.copy;

import lombok.Data;

import java.util.List;

@Data
public abstract class WithCoordinates extends WithComments {

    private final List<Coordinate> coordinates;

}
