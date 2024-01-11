package org.project.chats.exception;

import org.project.chats.type.ErrorMessage;

public class JwtException extends RuntimeException {
    public JwtException(ErrorMessage message) {
        super(message.getDescription());
    }
}
