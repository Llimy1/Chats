package org.project.chats.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRequestDto {

    private String roomName;

    public ChatRoomRequestDto(String roomName) {
        this.roomName = roomName;
    }
}
