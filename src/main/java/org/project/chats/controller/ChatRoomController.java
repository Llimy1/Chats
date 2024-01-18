package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.service.ChatRoomService;
import org.project.chats.service.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.project.chats.type.SuccessMessage.CHAT_ROOM_CREATE_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final CommonService commonService;

    @PostMapping("/chat/room")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createChatRoom() {
        log.debug("채팅 - 채팅방 개설 API 호출");
        String roomId = chatRoomService.createChatRoom();
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(
                        CHAT_ROOM_CREATE_SUCCESS.getDescription(), roomId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }
}
