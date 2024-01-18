package org.project.chats.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    @Builder
    public ChatRoom(String roomId) {
        this.roomId = roomId;
    }

    public static ChatRoom createChatRoom(String roomId) {
        return ChatRoom.builder()
                .roomId(roomId)
                .build();
    }
}
