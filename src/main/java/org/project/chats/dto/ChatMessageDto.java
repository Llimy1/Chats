package org.project.chats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
    private String roomId;
    private String sender;
    private String message;

    @Builder
    public ChatMessageDto(String sender, String roomId, String message) {
        this.sender = sender;
        this.roomId = roomId;
        this.message = message;
    }

    public void enterMessage() {
        this.message = this.sender + " 님이 입장합니다.";
    }

    public void quitMessage() {
        this.message = this.sender + " 님이 퇴장합니다.";
    }
}
