package org.project.chats.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    private String id;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime sendDate;
    private Boolean isRead;

    @Builder
    public ChatMessage(String roomId, String sender, String message, LocalDateTime sendDate, Boolean isRead) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.sendDate = sendDate;
        this.isRead = isRead;
    }

    public static ChatMessage createMessage(String roomId, String sender, String message, LocalDateTime sendDate, Boolean isRead) {
        return ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .sendDate(sendDate)
                .isRead(isRead)
                .build();
    }
}
