package net.iteach.api.model.copy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExportedTeacher {

    private static final int CURRENT_VERSION = 1;

    private final int version;
    private final List<ExportedSchool> schools;

    public ExportedTeacher(List<ExportedSchool> schools) {
        this(CURRENT_VERSION, schools);
    }
}
