package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatMessage;
import org.project.chats.domain.ChatRoomRedis;
import org.project.chats.domain.User;
import org.project.chats.dto.ChatMessageDto;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.ChatMessageRepository;
import org.project.chats.repository.ChatRoomRedisRepository;
import org.project.chats.repository.ChatRoomUserRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.type.ErrorMessage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void chatMessageSave(ChatMessageDto chatMessageDto) {
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

        chatMessageRepository.save(chatMessage);
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
}
