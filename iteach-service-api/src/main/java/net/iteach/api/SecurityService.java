package net.iteach.api;

import net.iteach.api.model.AuthenticationMode;

public interface SecurityService {

	void register(AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password);

	boolean isAdminInitialized();

	void init(String firstName, String lastName, String email, String password);

}
