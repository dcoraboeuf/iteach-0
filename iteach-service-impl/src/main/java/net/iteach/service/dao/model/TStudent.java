package net.iteach.service.dao.model;

import lombok.Data;

@Data
public class TStudent {
    private final int id;
    private final String subject;
    private final String name;
    private final int school;
    private final boolean disabled;
}
