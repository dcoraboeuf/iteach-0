package net.iteach.api.model.copy;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class School extends WithCoordinates {

    private final String name;
    private final String color;
    private final BigDecimal hrate;
    private final List<Student> students;

}
