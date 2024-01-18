package org.project.chats.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.repository.ChatRoomRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private static Map<String, List<WebSocketSession>> sessions = new HashMap<>();

    // 웹소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 새로운 인원 연결
        log.info("웹소켓 연결 = {}", session);

        String url = Objects.requireNonNull(session.getUri()).toString();
        int index = url.indexOf("?");
        String roomId = url.substring(index + 1);

        TextMessage textMessage = new TextMessage(session.getId() + " 님이 입장");

        List<WebSocketSession> roomSessions = sessions.getOrDefault(roomId, new ArrayList<>());
        roomSessions.add(session);

        roomSessions.forEach(s -> {
            try {
                s.sendMessage(textMessage);
            } catch (IOException e) {
                log.warn("Error sending message: {}", e.toString());
            }
        });

        sessions.put(roomId, roomSessions);
    }

    // 웹소켓 연결 해제
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {


        log.info("웹소켓 연결 해제 = {}", session);

        String url = Objects.requireNonNull(session.getUri()).toString();
        int index = url.indexOf("?");
        String roomId = url.substring(index + 1);

        // 퇴장 인원 전체 알림
        TextMessage textMessage = new TextMessage(session.getId() + " 님이 퇴장");

        List<WebSocketSession> roomSessions = sessions.get(roomId);
        roomSessions.remove(session);

        roomSessions.forEach(s -> {
            try {
                s.sendMessage(textMessage);
            } catch (IOException e) {
                log.warn("Error sending message: {}", e.toString());
            }
        });
    }

    // 양방향 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload = {}", payload);

        String url = Objects.requireNonNull(session.getUri()).toString();
        int index = url.indexOf("?");
        String roomId = url.substring(index + 1);

        List<WebSocketSession> roomSessions = sessions.get(roomId);
        roomSessions.forEach(s -> {
            try {
                s.sendMessage(message);
            } catch (IOException e) {
                // TODO : Exception
                log.warn("소켓 메세지 전송 오류");
            }
        });
    }

    // 소켓 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

}
