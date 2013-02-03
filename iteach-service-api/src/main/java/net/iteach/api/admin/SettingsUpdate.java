package net.iteach.api.admin;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.iteach.api.model.ConfigurationKey;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SettingsUpdate {

	private final Map<ConfigurationKey, String> values;

	public SettingsUpdate() {
		this(new HashMap<ConfigurationKey, String>());
	}

	public SettingsUpdate withValue(ConfigurationKey key, String value) {
		values.put(key, value);
		return this;
	}
	
	public String getValue (ConfigurationKey key) {
		return values.get(key);
	}

}
