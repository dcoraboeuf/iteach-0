package net.iteach.api.model.copy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class WithCoordinates extends WithComments {

    private final List<CoordinateCopy> coordinates;

    protected WithCoordinates(List<Comment> comments, List<CoordinateCopy> coordinates) {
        super(comments);
        this.coordinates = coordinates;
    }
}
