package net.iteach.api.model;

public enum ConfigurationKey {
	
	MAIL_REPLY_TO("noreply@iteach.net");
	
	private final String defaultValue;

	private ConfigurationKey(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}

}
