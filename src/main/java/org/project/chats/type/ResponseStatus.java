package org.project.shoppingmall.type;

import lombok.Getter;

@Getter
public enum ResponseStatus {

    SUCCESS("success"),
    FAIL("fail");

    private final String description;

    ResponseStatus(String description) {
        this.description = description;
    }
}
