package net.iteach.api;

import java.util.Locale;

import net.iteach.core.model.Ack;
import net.iteach.core.model.AuthenticationMode;
import net.iteach.core.model.TokenType;

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

	void passwordRequest(Locale locale, int userId);

	Ack passwordChange(int userId, String token, String oldPassword, String newPassword);

	void checkTokenForUserId(TokenType tokenType, String token, int userId);

}
