package net.iteach.api;

import java.util.Locale;

import net.iteach.api.model.AuthenticationMode;
import net.iteach.core.model.Ack;

public interface SecurityService {

	/**
	 * Asks the registration for a user
	 * @param locale Locale used for the registration
	 * @param mode Authentication mode
	 * @param identifier Identifier (depends on the authentication mode)
	 * @param firstName First name
	 * @param lastName Last name
	 * @param email Email of the user
	 * @param password Password (depends on the authentication mode)
	 * @return {@link Ack#OK} is the registration is complete and the account is ready for use. Returns {@link Ack#NOK} if the
	 * account must still be validated through email.
	 */
	Ack register(Locale locale, AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password);

	boolean isAdminInitialized();
	
	Ack completeRegistration (String token);

}
