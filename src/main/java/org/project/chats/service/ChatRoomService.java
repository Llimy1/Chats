package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatRoom;
import org.project.chats.domain.ChatRoomRedis;
import org.project.chats.domain.ChatRoomUser;
import org.project.chats.domain.User;
import org.project.chats.dto.request.ChatRoomRequestDto;
import org.project.chats.dto.response.ChatRoomInfoAndUserInfoResponseDto;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.ChatRoomRedisRepository;
import org.project.chats.repository.ChatRoomRepository;
import org.project.chats.repository.ChatRoomUserRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.project.chats.type.MessageType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public String createChatRoom(String accessToken, ChatRoomRequestDto chatRoomRequestDto) {
        log.info("create chat room service");

        String roomName = chatRoomRequestDto.roomName();
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
        String type = MessageType.REENTER.getDescription();;

        if (!userExist) {
            ChatRoomUser chatRoomUser = ChatRoomUser.createChatRoomUser(user, chatRoom);
            chatRoomUserRepository.save(chatRoomUser);
            type = MessageType.ENTER.getDescription();
        }

        ChatRoomRedis chatRoomRedis = ChatRoomRedis.builder()
                .roomId(chatRoom.getRoomId())
                .email(email)
                .build();

        // redis에 채팅방 입장 저장
        chatRoomRedisRepository.save(chatRoomRedis);

        return ChatRoomInfoAndUserInfoResponseDto.builder()
                .type(type)
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getName())
                .userName(user.getNickname())
                .build();
    }

    // 채팅방 퇴장
    @Transactional
    public void chatRoomQuit(String accessToken, String roomId) {
        log.info("채팅방 퇴장");

        // redis에 채팅방 정보 지우기
        String email = jwtUtil.getEmail(accessToken);
        ChatRoomRedis chatRoomRedis = chatRoomRedisRepository.findByRoomIdAndEmail(roomId, email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CHAT_ROOM_NOT_FOUND));

        chatRoomRedisRepository.delete(chatRoomRedis);
    }
}
