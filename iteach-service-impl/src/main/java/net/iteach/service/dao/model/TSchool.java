package net.iteach.service.dao.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TSchool {

    private final int id;
    private final String name;
    private final String color;
    private final BigDecimal hourlyRate;

}
