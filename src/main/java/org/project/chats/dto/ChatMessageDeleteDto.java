package org.project.chats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ChatMessageDeleteDto {
    private String id;
    private String roomId;
    private String sender;
    private Boolean isDeleted;

    @Builder
    public ChatMessageDeleteDto(String id, String roomId, String sender, Boolean isDeleted) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.isDeleted = isDeleted;
    }
}
