package net.iteach.core.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public interface SchoolFormValidation {

    @NotNull
    @Size(min=1, max=80)
    String getName();

    @NotNull
    @Pattern(regexp = "#[0-9A-Fa-f]{6}")
    String getColor();

}
