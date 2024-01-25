package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.ChatMessage;
import org.project.chats.dto.ChatMessageDto;
import org.project.chats.service.ChatMessageService;
import org.project.chats.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/enter")
    public void chatEnterMessage(ChatMessageDto chatMessageDto) {
        chatMessageDto.enterMessage();
        log.info("enter chat");
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                chatMessageDto);
    }

    @MessageMapping("/chat/send")
    public void chatSendMessage(ChatMessageDto chatMessageDto) {
        log.info("send message");
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                chatMessageDto);
        chatMessageService.chatMessageSave(chatMessageDto);
    }

    @MessageMapping("/chat/quit")
    public void chatQuitMessage(ChatMessageDto chatMessageDto) {
        chatMessageDto.quitMessage();
        log.info("quit chat");
        log.info("chat message = {}", chatMessageDto.getMessage());
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                chatMessageDto);
    }

}
