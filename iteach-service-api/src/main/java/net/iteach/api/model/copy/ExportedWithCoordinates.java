package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.iteach.core.model.Coordinate;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ExportedWithCoordinates extends ExportedWithComments {

    private final List<Coordinate> coordinates;

    protected ExportedWithCoordinates(List<ExportedComment> comments, List<Coordinate> coordinates) {
        super(comments);
        this.coordinates = coordinates;
    }
}
