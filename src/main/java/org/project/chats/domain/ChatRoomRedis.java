package org.project.chats.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "chatRoom")
public class ChatRoomRedis {

    @Id
    private String id;

    @Indexed
    private String roomId;
    @Indexed
    private String email;

    @Builder
    public ChatRoomRedis(String roomId, String email) {
        this.roomId = roomId;
        this.email = email;
    }
}
