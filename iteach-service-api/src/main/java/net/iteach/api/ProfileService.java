package net.iteach.api;

import java.util.Locale;

import net.iteach.core.model.AccountProfile;

public interface ProfileService {

	AccountProfile getProfile();

	AccountProfile getProfile(int userId);

	void passwordRequest(Locale locale);

	void passwordChangeCheck(String token);

	void passwordChange(String token, String oldPassword, String newPassword);

}
