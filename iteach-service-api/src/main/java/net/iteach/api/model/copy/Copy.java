package net.iteach.api.model.copy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Copy {

    private static final int CURRENT_VERSION = 1;

    private final int version;
    private final List<School> schools;

    public Copy(List<School> schools) {
        this(CURRENT_VERSION, schools);
    }
}
