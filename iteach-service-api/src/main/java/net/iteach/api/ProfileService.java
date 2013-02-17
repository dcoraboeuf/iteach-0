package net.iteach.api;

import net.iteach.core.model.AccountProfile;
import net.iteach.core.model.Ack;
import net.iteach.core.model.PreferenceKey;

import java.util.Locale;
import java.util.Map;

public interface ProfileService {

    AccountProfile getProfile();

    AccountProfile getProfile(int userId);

    void passwordRequest(Locale locale);

    void passwordChangeCheck(String token);

    Ack passwordChange(String token, String oldPassword, String newPassword);

    void savePreferences(Map<PreferenceKey, String> preferences);
}
