package net.iteach.api;

import net.iteach.api.model.ConfigurationKey;

public interface ConfigurationService {
	
	String getConfigurationValue (ConfigurationKey key);
	
	void setConfigurationValue (ConfigurationKey key, String value);

}
