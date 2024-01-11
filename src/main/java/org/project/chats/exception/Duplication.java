package org.project.shoppingmall.exception;

import org.project.shoppingmall.type.ErrorMessage;

public class Duplication extends RuntimeException {
    public Duplication(ErrorMessage message) {
        super(message.getDescription());
    }

}
