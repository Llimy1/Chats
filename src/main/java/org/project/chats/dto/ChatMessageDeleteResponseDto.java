package org.project.chats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChatMessageDeleteResponseDto {
    private String id;
    private String roomId;
    private String sender;
    private String message;
    private Boolean isDeleted;

    @Builder
    public ChatMessageDeleteResponseDto(String id, String roomId, String sender, String message, Boolean isDeleted) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.isDeleted = isDeleted;
    }
}
