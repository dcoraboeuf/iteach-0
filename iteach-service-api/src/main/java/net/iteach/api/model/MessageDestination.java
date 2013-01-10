package net.iteach.api.model;

import lombok.Data;

@Data
public class MessageDestination {

	private final MessageChannel channel;
	private final String destination;

}
