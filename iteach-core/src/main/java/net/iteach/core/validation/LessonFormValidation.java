package net.iteach.core.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface LessonFormValidation {

    @NotNull
    LocalDate getDate();

    @NotNull
    LocalTime getFrom();

    @NotNull
    LocalTime getTo();
    
    @Size(min = 0, max = 80)
    String getLocation();

}
