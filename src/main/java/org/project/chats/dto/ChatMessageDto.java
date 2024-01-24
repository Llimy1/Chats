package org.project.chats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.chats.type.MessageType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
    private String roomId;
    private String sender;
    private String message;

    @Builder
    public ChatMessageDto(String roomId, String sender, String message) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }

    public void enterMessage() {
        this.message = this.sender + " 님이 입장합니다.";
    }

    public void quitMessage() {
        this.message = this.sender + " 님이 퇴장합니다.";
    }
}
