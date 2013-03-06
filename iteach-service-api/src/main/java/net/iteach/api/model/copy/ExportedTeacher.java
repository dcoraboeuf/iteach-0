package net.iteach.api.model.copy;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Data
public class ExportedTeacher {

    private static final int CURRENT_VERSION = 1;

    private final int version;
    private final List<ExportedSchool> schools;

    @JsonCreator
    public ExportedTeacher(
            @JsonProperty("version")
            int version,
            @JsonProperty("schools")
            List<ExportedSchool> schools) {
        this.version = version;
        this.schools = schools;
    }

    public ExportedTeacher(
            List<ExportedSchool> schools) {
        this(CURRENT_VERSION, schools);
    }
}
