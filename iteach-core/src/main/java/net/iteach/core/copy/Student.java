package net.iteach.core.copy;

import lombok.Data;

import java.util.List;

@Data
public class Student extends WithCoordinates {

    private final String name;
    private final String subject;
    private final boolean disabled;
    private final List<Lesson> lessons;

}
