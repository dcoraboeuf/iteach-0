package net.iteach.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import net.iteach.api.model.MessageChannel;
import net.iteach.core.RunProfile;
import net.iteach.core.model.Message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Collects all the messages in the in-memory box, used for test only
 *
 */
@Component
@Profile({RunProfile.TEST, RunProfile.IT})
public class TestPost extends AbstractMessagePost {
	
	private final Map<String, Message> messages = new LinkedHashMap<String, Message>();

	/**
	 * Supports all channels
	 */
	@Override
	public boolean supports(MessageChannel channel) {
		return true;
	}

	@Override
	public synchronized void post(Message message, String destination) {
		messages.put(destination, message);
	}
	
	public Message getMessage (String email) {
		return messages.get(email);
	}

}
