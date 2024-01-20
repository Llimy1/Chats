package org.project.chats.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Slf4j
@Configuration
public class WebSocketEventConfig {

    @EventListener
    public void handleWebSocketEventListener(SessionConnectedEvent sessionConnectedEvent) {
        StompHeaderAccessor stompHeaderAccessor =
                StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        log.info("WebSocket session = {}", stompHeaderAccessor.getSessionAttributes());
        log.info("WebSocket session id = {}", stompHeaderAccessor.getId());
    }
}
