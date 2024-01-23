package org.project.chats.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collation = "chatting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime sendData;

    @Builder
    public ChatMessage(String roomId, String sender, String message, LocalDateTime sendData) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.sendData = sendData;
    }
}
