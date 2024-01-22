package org.project.chats.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomInfoAndUserInfoResponseDto {

    private String roomId;
    private String roomName;
    private String userName;

    @Builder
    public ChatRoomInfoAndUserInfoResponseDto(String roomId, String roomName, String userName) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.userName = userName;
    }
}
