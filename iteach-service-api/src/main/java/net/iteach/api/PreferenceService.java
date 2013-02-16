package net.iteach.api;

import net.iteach.core.model.PreferenceKey;
import net.iteach.core.model.Preferences;

public interface PreferenceService {
	
	String getPreference (PreferenceKey key);

    int getPreferenceAsInt(PreferenceKey key);
	
	void setPreference (PreferenceKey key, String value);

    void setPreference(PreferenceKey key, int value);

    Preferences getPreferences();
}
