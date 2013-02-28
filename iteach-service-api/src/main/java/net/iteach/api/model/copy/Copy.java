package net.iteach.api.model.copy;

import lombok.Data;

import java.util.List;

@Data
public class Copy {

    private static final int CURRENT_VERSION = 1;

    private final int version;
    private final List<School> schools;

}
