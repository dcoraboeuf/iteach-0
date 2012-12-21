package net.iteach.core.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public interface StudentFormValidation {

    @NotNull
    @Size(min=1, max=40)
    String getFirstName();

    @NotNull
    @Size(min=1, max=40)
    String getLastName();

    @NotNull
    @Size(min=0, max=80)
    String getSubject();

}
