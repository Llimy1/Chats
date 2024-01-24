package org.project.chats.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomInfoAndUserInfoResponseDto {

    private String type;
    private String roomId;
    private String roomName;
    private String userName;

    @Builder
    public ChatRoomInfoAndUserInfoResponseDto(String type, String roomId, String roomName, String userName) {
        this.type = type;
        this.roomId = roomId;
        this.roomName = roomName;
        this.userName = userName;
    }
}
