package org.project.chats.type;

import lombok.Getter;

@Getter
public enum MessageType {
    ENTER("enter"),
    REENTER("re-ender"),
    SEND("sender"),
    QUIT("quit");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }
}
