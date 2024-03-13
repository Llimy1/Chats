package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.ChatMessageDeleteDto;
import org.project.chats.dto.ChatMessageDeleteResponseDto;
import org.project.chats.dto.ChatMessageDto;
import org.project.chats.dto.ChatMessageUpdateDto;
import org.project.chats.dto.response.ChatMessageResponseDto;
import org.project.chats.dto.response.MessageResponseDto;
import org.project.chats.dto.rtc.AnswerDto;
import org.project.chats.dto.rtc.IceCandidateDto;
import org.project.chats.dto.rtc.OfferDto;
import org.project.chats.service.ChatMessageService;
import org.project.chats.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
        MessageResponseDto messageResponseDto = chatMessageService.chatMessageSave(chatMessageDto);
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                messageResponseDto);
    }

    @MessageMapping("/chat/update")
    public void chatUpdateMessage(ChatMessageUpdateDto chatMessageUpdateDto) {
        log.info("update message");
        chatMessageService.update(chatMessageUpdateDto);
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageUpdateDto.getRoomId(),
                chatMessageUpdateDto);
    }

    @MessageMapping("/chat/delete")
    public void chatDeletedMessage(ChatMessageDeleteDto chatMessageDeleteDto) {
        log.info("delete message");
        ChatMessageDeleteResponseDto chatMessageDeleteResponseDto = chatMessageService.deleteMessage(chatMessageDeleteDto);
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDeleteDto.getRoomId(),
                chatMessageDeleteResponseDto);
    }

    @MessageMapping("/chat/quit")
    public void chatQuitMessage(ChatMessageDto chatMessageDto) {
        chatMessageDto.quitMessage();
        log.info("quit chat");
        log.info("chat message = {}", chatMessageDto.getMessage());
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(),
                chatMessageDto);
    }

    //offer 정보를 주고 받기 위한 websocket
    //camKey : 각 요청하는 캠의 key , roomId : 룸 아이디
    @MessageMapping("/peer/offer/{camKey}/{roomId}")
    @SendTo("/sub/peer/offer/{camKey}/{roomId}")
    public String PeerHandleOffer(@Payload String offer, @DestinationVariable(value = "roomId") String roomId,
                                  @DestinationVariable(value = "camKey") String camKey) {
        log.info("[OFFER] {} : {}", camKey, offer);
        return offer;
    }

    //iceCandidate 정보를 주고 받기 위한 webSocket
    //camKey : 각 요청하는 캠의 key , roomId : 룸 아이디
    @MessageMapping("/peer/iceCandidate/{camKey}/{roomId}")
    @SendTo("/sub/peer/iceCandidate/{camKey}/{roomId}")
    public String PeerHandleIceCandidate(@Payload String candidate, @DestinationVariable(value = "roomId") String roomId,
                                         @DestinationVariable(value = "camKey") String camKey) {
        log.info("[ICECANDIDATE] {} : {}", camKey, candidate);
        return candidate;
    }

    //

    @MessageMapping("/peer/answer/{camKey}/{roomId}")
    @SendTo("/sub/peer/answer/{camKey}/{roomId}")
    public String PeerHandleAnswer(@Payload String answer, @DestinationVariable(value = "roomId") String roomId,
                                   @DestinationVariable(value = "camKey") String camKey) {
        log.info("[ANSWER] {} : {}", camKey, answer);
        return answer;
    }

    //camKey 를 받기위해 신호를 보내는 webSocket
    @MessageMapping("/call/key")
    @SendTo("/sub/call/key")
    public String callKey(@Payload String message) {
        log.info("[Key] : {}", message);
        return message;
    }

    //자신의 camKey 를 모든 연결된 세션에 보내는 webSocket
    @MessageMapping("/send/key")
    @SendTo("/sub/send/key")
    public String sendKey(@Payload String message) {
        return message;
    }
}
