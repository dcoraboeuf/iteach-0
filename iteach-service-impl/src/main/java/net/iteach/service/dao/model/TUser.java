package net.iteach.service.dao.model;

import lombok.Data;
import net.iteach.core.model.AuthenticationMode;

@Data
public class TUser {

    private final int id;
    private final AuthenticationMode mode;
    private final String identifier;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final boolean administrator;
    private final boolean verified;

}
