package org.project.chats.type;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
