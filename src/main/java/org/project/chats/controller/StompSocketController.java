package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/enter")
    public void chatEnterMessage(ChatMessageDto chatMessageDto) {
        chatMessageDto.enterMessage();
        log.info("chat message = {}", chatMessageDto.getMessage());
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                chatMessageDto);
    }

    @MessageMapping("/chat/send")
    public void chatSendMessage(ChatMessageDto chatMessageDto) {
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                chatMessageDto);
    }

}
