package net.iteach.api;

import net.iteach.api.model.MessageDestination;
import net.iteach.core.model.Message;


public interface MessageService {

	void sendMessage(Message message, MessageDestination messageDestination);

}
