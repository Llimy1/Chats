package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.config.websocket.WebSocketHandler;
import org.project.chats.domain.ChatRoom;
import org.project.chats.domain.User;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.ChatRoomRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public String createChatRoom() {
        log.info("채팅 룸 생성");
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.createChatRoom(randomId);
        chatRoomRepository.save(chatRoom);
        log.debug("chatRoom Id = {}", chatRoom.getId());
        return chatRoom.getRoomId();
    }
}
