package net.iteach.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.iteach.api.model.MessageChannel;
import net.iteach.core.RunProfile;
import net.iteach.core.model.Message;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@Profile(RunProfile.PROD)
public class MailPost extends AbstractMessagePost {

	private final Logger logger = LoggerFactory.getLogger(MailPost.class);

	private final JavaMailSender mailSender;

	@Autowired
	public MailPost(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public boolean supports(MessageChannel channel) {
		return (channel == MessageChannel.EMAIL);
	}

	@Override
	public void post(final Message message, final String destination) {
		
		// TODO Reply-to address
		final String replyToAddress = "iteach@test.com";
		logger.debug("[mail] Sending message from: {}", replyToAddress);

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				prepareMessage(mimeMessage, message, destination, replyToAddress);
			}
		};
		try {
			this.mailSender.send(preparator);
		} catch (MailException ex) {
			logger.error("[mail] Cannot send mail: {}", ExceptionUtils.getRootCauseMessage(ex));
		}
	}

	protected void prepareMessage(MimeMessage mimeMessage, Message message, String destination, String replyToAddress) throws MessagingException, AddressException {
		mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destination));
		mimeMessage.setFrom(new InternetAddress(replyToAddress));
		mimeMessage.setSubject(message.getTitle());
		mimeMessage.setText(message.getContent().getText());
	}

}
