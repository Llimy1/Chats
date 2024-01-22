package org.project.chats.dto.request;

import lombok.Getter;

@Getter
public class ChatRoomIdRequestDto {

    private String roomId;

    public ChatRoomIdRequestDto(String roomId) {
        this.roomId = roomId;
    }
}
