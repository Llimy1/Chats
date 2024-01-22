package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatRoom;
import org.project.chats.domain.ChatRoomUser;
import org.project.chats.domain.User;
import org.project.chats.dto.request.ChatRoomRequestDto;
import org.project.chats.dto.response.ChatRoomInfoAndUserInfoResponseDto;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.ChatRoomRepository;
import org.project.chats.repository.ChatRoomUserRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomUserRepository chatRoomUserRepository;
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
        chatRooms.forEach(c -> chatRoomNames.add(c.getName()));

        return chatRoomNames;
    }

    @Transactional
    public ChatRoomInfoAndUserInfoResponseDto chatRoomInfoAndUserInfo(String accessToken, String roomName) {
        log.info("채팅방 입장");
        ChatRoom chatRoom = chatRoomRepository.findByName(roomName)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CHAT_ROOM_NOT_FOUND));

        String email = jwtUtil.getEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        Long userId = user.getId();

        boolean userExist = chatRoomUserRepository.existsByUserId(userId);

        if (!userExist) {
            ChatRoomUser chatRoomUser = ChatRoomUser.createChatRoomUser(user, chatRoom);
            chatRoomUserRepository.save(chatRoomUser);
        }

        return ChatRoomInfoAndUserInfoResponseDto.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getName())
                .userName(user.getNickname())
                .build();
    }
}
