package net.iteach.api;

import java.util.Locale;

import net.iteach.api.model.AuthenticationMode;
import net.iteach.core.model.RegistrationCompletionForm;

public interface SecurityService {

	void register(Locale locale, AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password);

	boolean isAdminInitialized();

	RegistrationCompletionForm getRegistrationCompletionForm(String token);

}
