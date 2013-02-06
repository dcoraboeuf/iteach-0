package net.iteach.service.impl;

import net.iteach.api.CommunicationService;
import net.iteach.api.ConfigurationService;
import net.iteach.api.MessageService;
import net.iteach.api.model.ConfigurationKey;
import net.iteach.api.model.ErrorMessage;
import net.iteach.api.model.MessageChannel;
import net.iteach.api.model.MessageDestination;
import net.iteach.core.model.Ack;
import net.iteach.core.model.Message;
import net.iteach.core.model.MessageContent;
import net.iteach.core.model.TokenType;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.token.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final SecurityUtils securityUtils;
    private final TokenService tokenService;
    private final ConfigurationService configurationService;
    private final MessageService messageService;

    @Autowired
    public CommunicationServiceImpl(SecurityUtils securityUtils, TokenService tokenService, ConfigurationService configurationService, MessageService messageService) {
        this.securityUtils = securityUtils;
        this.tokenService = tokenService;
        this.configurationService = configurationService;
        this.messageService = messageService;
    }

    @Override
    public String sendError(ErrorMessage error) {
        // Generates a token
        String token = tokenService.generateToken(TokenType.ERROR, error.getUuid());
        // Sends an email to the admin in any case
        // Builds the message (in English, no need for a template here)
        Message m = new Message(
                String.format("Error report: %s", error.getUuid()),
                new MessageContent(
                        String.format(
                                "Error in the application.\nUUID: %s\nReport available: %s\nServer message: %s\n",
                                error.getUuid(),
                                error.isReport(),
                                error.getMessage()
                        ),
                        null,
                        token
                )
        );
        // Gets the email of the administrator
        String email = configurationService.getConfigurationValue(ConfigurationKey.MAIL_REPLY_TO);
        // Sends the message
        messageService.sendMessage(m, new MessageDestination(MessageChannel.EMAIL, email));
        // Returns the token
        return token;
    }

    @Override
    public Ack sendErrorMessage(String token, String uuid, String error, String message) {
        // Consumes the token
        tokenService.consumesToken(token, TokenType.ERROR, uuid);
        // Identity of the current user
        String username = securityUtils.getCurrentUser().getUsername();
        if (StringUtils.isBlank(username)) {
            username = "Not available";
        }
        // Builds the message (in English, no need for a template here)
        Message m = new Message(
                String.format("Error user message for %s", uuid),
                new MessageContent(
                    String.format(
                        "Error message sent by user %s.\nUUID: %s\nServer message: %s\nUser message: %s\n",
                        username,
                        uuid,
                        error,
                        message
                    ),
                    null,
                    token
                )
        );
        // Gets the email of the administrator
        String email = configurationService.getConfigurationValue(ConfigurationKey.MAIL_REPLY_TO);
        // Sends the message
        messageService.sendMessage(m, new MessageDestination(MessageChannel.EMAIL, email));
        // OK
        return Ack.OK;
    }

}
