package org.project.chats.exception;


import org.project.chats.type.ErrorMessage;

public class Duplication extends RuntimeException {
    public Duplication(ErrorMessage message) {
        super(message.getDescription());
    }

}
