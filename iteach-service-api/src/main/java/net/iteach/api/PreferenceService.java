package net.iteach.api;

import net.iteach.core.model.PreferenceKey;

public interface PreferenceService {
	
	String getPreference (PreferenceKey key);

    int getPreferenceAsInt(PreferenceKey key);
	
	void setPreference (PreferenceKey key, String value);

    void setPreference(PreferenceKey key, int value);
}
