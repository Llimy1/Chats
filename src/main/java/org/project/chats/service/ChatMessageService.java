package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatMessage;
import org.project.chats.domain.ChatRoomRedis;
import org.project.chats.domain.User;
import org.project.chats.dto.ChatMessageDeleteDto;
import org.project.chats.dto.ChatMessageDeleteResponseDto;
import org.project.chats.dto.ChatMessageDto;
import org.project.chats.dto.ChatMessageUpdateDto;
import org.project.chats.dto.response.MessageResponseDto;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.ChatMessageRepository;
import org.project.chats.repository.ChatRoomRedisRepository;
import org.project.chats.repository.ChatRoomUserRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final JwtUtil jwtUtil;

    @Transactional
    public MessageResponseDto chatMessageSave(ChatMessageDto chatMessageDto) {
        boolean isRead = currentChatUserCount(chatMessageDto.getRoomId()) == allChatUserCount(chatMessageDto.getRoomId());

        String sender = chatMessageDto.getSender();
        User findUser = userRepository.findByNickname(sender)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        notificationService.chatNotification(chatMessageDto.getMessage(),
                chatMessageDto.getRoomId(), false, findUser, findUser);

        log.info("채팅 저장 = {}", chatMessageDto.getMessage());
        ChatMessage chatMessage = ChatMessage.createMessage(
                chatMessageDto.getRoomId(),
                chatMessageDto.getSender(),
                chatMessageDto.getMessage(),
                LocalDateTime.now(),
                isRead
        );

        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);
        return MessageResponseDto.builder()
                .id(saveMessage.getId())
                .sender(chatMessageDto.getSender())
                .roomId(chatMessageDto.getRoomId())
                .message(chatMessageDto.getMessage())
                .isEdited(saveMessage.getIsEdited())
                .isDeleted(saveMessage.getIsDeleted())
                .build();
    }

    public List<ChatMessage> selectChatMessage(String roomId) {
        log.info("저장된 채팅 가져오기 roomId = {}", roomId);
        updateIsReadTrue(roomId);
        return chatMessageRepository.findAllByRoomId(roomId);
    }

    // 채팅방의 채팅 모두 읽음으로 변경
    @Transactional
    public void updateIsReadTrue(String roomId) {
        log.info("채팅 모두 유저 읽음");
        if (currentChatUserCount(roomId) == allChatUserCount(roomId)) {
            Query query = new Query(Criteria.where("roomId").is(roomId));
            Update update = new Update().set("isRead", true);
            mongoTemplate.updateMulti(query, update, ChatMessage.class);
        }
    }

    // 채팅방 현재 접속 인원
    private int currentChatUserCount(String roomId) {
        List<ChatRoomRedis> chatRoomRedisList = chatRoomRedisRepository.findAllByRoomId(roomId);
        log.info("채팅방 접속 유저 수 = {}", chatRoomRedisList.size());
        return chatRoomRedisList.size();
    }

    // 채팅방 전체 인원
    private int allChatUserCount(String roomId) {
        int allChatUserCount = chatRoomUserRepository.countChatRoomUserByRoomId(roomId);
        log.info("채팅방 총 유저 수 = {}", allChatUserCount);
        return allChatUserCount;
    }

    public void enter(String accessToken) {

        Optional<User> byEmail = userRepository.findByEmail(jwtUtil.getEmail(accessToken));
        User user = byEmail.get();

        notificationService.enter(user, user.getNickname() + "님이 입장했습니다.");
    }

    public void update(ChatMessageUpdateDto chatMessageUpdateDto) {
        String id = chatMessageUpdateDto.getId();
        String roomId = chatMessageUpdateDto.getRoomId();
        String sender = chatMessageUpdateDto.getSender();
        String message = chatMessageUpdateDto.getMessage();
        Boolean isEdited = chatMessageUpdateDto.getIsEdited();
        Query query = new Query(Criteria.where("id").is(id)
                .and("roomId").is(roomId)
                .and("sender").is(sender));
        Update update = new Update().set("message", message).set("isEdited", isEdited);

        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }

    public ChatMessageDeleteResponseDto deleteMessage(ChatMessageDeleteDto chatMessageDeleteDto) {
        String id = chatMessageDeleteDto.getId();
        String roomId = chatMessageDeleteDto.getRoomId();
        String sender = chatMessageDeleteDto.getSender();
        Boolean isDeleted = chatMessageDeleteDto.getIsDeleted();

        String message = "삭제된 메세지입니다.";
        Query query = new Query(Criteria.where("id").is(id)
                .and("roomId").is(roomId)
                .and("sender").is(sender));
        Update update = new Update().set("message", message).set("isDeleted", isDeleted);

        mongoTemplate.updateMulti(query, update, ChatMessage.class);

        return ChatMessageDeleteResponseDto.builder()
                .id(id)
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .isDeleted(isDeleted)
                .build();
    }
}
