package org.project.chats.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.chats.domain.ChatMessage;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {

    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime sendDate;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getRoomId();
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
        this.sendDate = chatMessage.getSendDate();
    }
}
