package net.iteach.core.validation;

import javax.validation.constraints.NotNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface LessonFormValidation {

    @NotNull
    LocalDate getDate();

    @NotNull
    LocalTime getFrom();

    @NotNull
    LocalTime getTo();

}
