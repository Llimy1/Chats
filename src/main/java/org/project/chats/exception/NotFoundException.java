package org.project.chats.exception;

import org.project.chats.type.ErrorMessage;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage.getDescription());
    }
}
