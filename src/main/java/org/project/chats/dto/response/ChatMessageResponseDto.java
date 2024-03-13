package org.project.chats.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.chats.domain.ChatMessage;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {

    private String id;
    private String roomId;
    private String sender;
    private String message;
    private Boolean isEdited;
    private Boolean isDeleted;
    private LocalDateTime sendDate;

    @Builder
    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.roomId = chatMessage.getRoomId();
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
        this.sendDate = chatMessage.getSendDate();
        this.isEdited = chatMessage.getIsEdited();
        this.isDeleted = chatMessage.getIsDeleted();
    }
}
