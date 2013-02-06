package net.iteach.api;

import net.iteach.api.model.ErrorMessage;
import net.iteach.core.model.Ack;

public interface CommunicationService {

    Ack sendErrorMessage(String token, String uuid, String error, String message);

    String sendError(ErrorMessage error);
}
