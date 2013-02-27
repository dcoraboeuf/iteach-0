package net.iteach.core.copy;

import lombok.Data;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Data
public class Lesson extends WithComments {

    private final LocalDate date;
    private final LocalTime from;
    private final LocalTime to;
    private final String location;

}
