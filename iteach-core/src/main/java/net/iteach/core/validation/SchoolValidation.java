package net.iteach.core.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface SchoolValidation {

    @NotNull
    @Size(min=1, max=80)
    String getName();

    @NotNull
    @Size(min=7, max=7)
    String getColor();

}
