package org.project.chats.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketMessage {
    private String sender;
    private String roomId;
    private String message;

    @Builder
    public SocketMessage(String sender, String roomId, String message) {
        this.sender = sender;
        this.roomId = roomId;
        this.message = message;
    }
}
