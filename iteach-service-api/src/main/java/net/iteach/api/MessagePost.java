package net.iteach.api;

import net.iteach.api.model.MessageChannel;
import net.iteach.core.model.Message;

public interface MessagePost {
	
	boolean supports (MessageChannel channel);
	
	void post (Message message, String destination);

}
