package net.iteach.service.token;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class TokenKey {
	
	private final String key;
	private final DateTime creation;

}
