package net.iteach.api;

import java.util.Locale;

import net.iteach.core.model.AccountProfile;
import net.iteach.core.model.Ack;

public interface ProfileService {

	AccountProfile getProfile();

	AccountProfile getProfile(int userId);

	void passwordRequest(Locale locale);

	void passwordChangeCheck(String token);

	Ack passwordChange(String token, String oldPassword, String newPassword);

}
