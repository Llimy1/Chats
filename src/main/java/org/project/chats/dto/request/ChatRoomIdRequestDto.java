package org.project.chats.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomIdRequestDto {

    private String roomId;

    public ChatRoomIdRequestDto(String roomId) {
        this.roomId = roomId;
    }
}
