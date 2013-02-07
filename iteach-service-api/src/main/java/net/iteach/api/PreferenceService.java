package net.iteach.api;

import net.iteach.core.model.PreferenceKey;

public interface PreferenceService {
	
	String getPreference (PreferenceKey key);
	
	void setPreference (PreferenceKey key, String value);

}
