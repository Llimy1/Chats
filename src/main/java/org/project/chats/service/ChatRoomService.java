package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatRoom;
import org.project.chats.domain.User;
import org.project.chats.dto.request.ChatRoomIdRequestDto;
import org.project.chats.dto.request.ChatRoomRequestDto;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.ChatRoomRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String createChatRoom(String accessToken, ChatRoomRequestDto chatRoomRequestDto) {
        log.info("create chat room service");

        String roomName = chatRoomRequestDto.getRoomName();
        String email = jwtUtil.getEmail(accessToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));


        ChatRoom chatRoom = ChatRoom.createChatRoom(roomName, user);
        chatRoomRepository.save(chatRoom);

        return chatRoom.getRoomId();
    }

    public List<String> chatRoomNameList() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<String> chatRoomNames = new ArrayList<>();
        chatRooms.forEach(c -> {
            chatRoomNames.add(c.getName());
        });

        return chatRoomNames;
    }

    public String chatRoomId(String roomName) {
        ChatRoom chatRoom = chatRoomRepository.findByName(roomName)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CHAT_ROOM_NOT_FOUND));

        return chatRoom.getRoomId();
    }
}
