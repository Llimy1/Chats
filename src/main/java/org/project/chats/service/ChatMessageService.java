package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatMessage;
import org.project.chats.dto.ChatMessageDto;
import org.project.chats.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void chatMessageSave(ChatMessageDto chatMessageDto) {

        log.info("채팅 저장 = {}", chatMessageDto.getMessage());
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatMessageDto.getRoomId())
                .sender(chatMessageDto.getSender())
                .message(chatMessageDto.getMessage())
                .sendData(LocalDateTime.now())
                .build();

        chatMessageRepository.save(chatMessage);
    }

    public void selectChatMessage(String roomId) {
        log.info("저장된 채팅 가져오기 roomId = {}", roomId);
    }
}
