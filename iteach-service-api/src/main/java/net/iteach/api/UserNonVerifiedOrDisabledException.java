package net.iteach.api;

import org.springframework.security.core.AuthenticationException;

public class UserNonVerifiedOrDisabledException extends AuthenticationException {
    public UserNonVerifiedOrDisabledException() {
        super("User is disabled or not verified");
    }
}
