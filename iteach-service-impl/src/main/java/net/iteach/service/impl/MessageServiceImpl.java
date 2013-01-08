package net.iteach.service.impl;

import java.util.List;

import net.iteach.api.MessagePost;
import net.iteach.api.MessageService;
import net.iteach.api.model.MessageChannel;
import net.iteach.api.model.MessageDestination;
import net.iteach.core.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

	private final List<MessagePost> posts;

	@Autowired
	public MessageServiceImpl(List<MessagePost> posts) {
		this.posts = posts;
	}

	@Override
	public void sendMessage(Message message, MessageDestination messageDestination) {
		MessageChannel channel = messageDestination.getChannel();
		String destination = messageDestination.getDestination();
		for (MessagePost post : posts) {
			if (post.supports(channel)) {
				post.post(message, destination);
			}
		}
	}

}
