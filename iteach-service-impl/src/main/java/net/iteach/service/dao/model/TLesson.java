package net.iteach.service.dao.model;

import lombok.Data;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Data
public class TLesson {

    private final int id;
    private final int student;
    private final LocalDate date;
    private final LocalTime from;
    private final LocalTime to;
    private final String location;

}
