package org.project.chats.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.exception.JwtException;
import org.project.chats.service.ChatMessageService;
import org.project.chats.service.jwt.JwtUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.project.chats.type.ErrorMessage.JWT_EXPIRATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChatMessageService chatMessageService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("StompHeaderAccessor = {}", accessor);
        handleMessage(Objects.requireNonNull(accessor.getCommand()), accessor);
        return message;
    }

    private void handleMessage(StompCommand command, StompHeaderAccessor accessor) {
        switch (command) {
            case SUBSCRIBE:
                chatMessageService.enter(getAccessToken(accessor));
            case SEND:
                verifyToken(getAccessToken(accessor));
                break;
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }

    private void verifyToken(String accessToken) {
        if (!jwtUtil.verifyToken(accessToken)) {
            throw new JwtException(JWT_EXPIRATION);
        }
    }
}
