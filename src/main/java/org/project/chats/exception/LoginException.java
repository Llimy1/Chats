package org.project.chats.exception;

import org.project.chats.type.ErrorMessage;

public class LoginException extends RuntimeException {
    public LoginException(ErrorMessage message) {
        super(message.getDescription());
    }
}
