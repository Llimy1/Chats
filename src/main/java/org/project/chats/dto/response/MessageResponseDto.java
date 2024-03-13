package org.project.chats.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.project.chats.domain.ChatMessage;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private String id;
    private String roomId;
    private String sender;
    private String message;
    private Boolean isEdited;
    private Boolean isDeleted;

    @Builder
    public MessageResponseDto(String id, String roomId, String sender, String message, Boolean isEdited, Boolean isDeleted) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.isEdited = isEdited;
        this.isDeleted = isDeleted;
    }
}
