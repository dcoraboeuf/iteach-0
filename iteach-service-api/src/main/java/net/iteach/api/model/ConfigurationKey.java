package net.iteach.api.model;

public enum ConfigurationKey {
	
	MAIL_REPLY_TO("noreply@iteach.net", "email");
	
	private final String defaultValue;
	private final String type;

	private ConfigurationKey(String defaultValue) {
		this(defaultValue, "text");
	}

	private ConfigurationKey(String defaultValue, String type) {
		this.defaultValue = defaultValue;
		this.type = type;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public String getType() {
		return type;
	}

}
