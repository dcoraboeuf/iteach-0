package net.iteach.api.admin;

import net.iteach.api.model.ConfigurationKey;
import lombok.Data;

@Data
public class Setting {
	
	private final ConfigurationKey key;
	private final String defaultValue;
	private final String value;
	private final String type;

}
