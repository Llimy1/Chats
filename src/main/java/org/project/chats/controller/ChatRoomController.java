package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.dto.request.ChatRoomRequestDto;
import org.project.chats.dto.response.ChatMessageResponseDto;
import org.project.chats.dto.response.ChatRoomInfoAndUserInfoResponseDto;
import org.project.chats.service.ChatMessageService;
import org.project.chats.service.ChatRoomService;
import org.project.chats.service.CommonService;
import org.project.chats.type.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final CommonService commonService;

    @PostMapping("/chat/room")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createChatRoom(@RequestHeader("Authorization") String accessToken,
                                                 @RequestBody ChatRoomRequestDto chatRoomRequestDto) {

        log.info("채팅방 생성 API 호출");
        String roomId = chatRoomService.createChatRoom(accessToken, chatRoomRequestDto);
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(SuccessMessage.CHAT_ROOM_CREATE_SUCCESS.getDescription(),
                        roomId);

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }

    @GetMapping("/chat/room")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> selectChatRoomName() {
        log.info("채팅방 조회 API 호출");
        List<String> chatRoomNames = chatRoomService.chatRoomNameList();
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(SuccessMessage.CHAT_ROOM_SELECT_SUCCESS.getDescription(),
                        chatRoomNames);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }

    @PostMapping("/chat/{roomName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> selectChatRoomInfoAndUserInfo(@RequestHeader("Authorization") String accessToken,
                                                   @PathVariable String roomName) {
        log.info("채팅방 및 유저 반환과 저장 API 호출");
        ChatRoomInfoAndUserInfoResponseDto chatRoomInfoAndUserInfoResponseDto =
                chatRoomService.chatRoomInfoAndUserInfo(accessToken, roomName);
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(
                        SuccessMessage.CHAT_ROOM_ID_SELECT_SUCCESS.getDescription(),
                       chatRoomInfoAndUserInfoResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }

    @GetMapping("/chat/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> selectChatMessage(@PathVariable String roomId) {

        log.info("저장된 채팅방 메세지 반환 API 호출");
        List<ChatMessageResponseDto> result = chatMessageService.selectChatMessage(roomId).stream()
                .map(ChatMessageResponseDto::new)
                .toList();
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(
                        SuccessMessage.CHAT_MESSAGE_SELECT_SUCCESS.getDescription(),
                        result
                );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }

    @PostMapping("/chat/redis/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteChatRoomRedisInfo(@RequestHeader("Authorization") String accessToken,
                                                          @PathVariable String roomId) {
        log.info("Redis Chat Room 정보 삭제 - 채팅방 떠남");
        chatRoomService.chatRoomQuit(accessToken, roomId);
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(
                        SuccessMessage.REDIS_CHAT_ROOM_INSERT_INFO_DELETE_SUCCESS.getDescription(),
                        null
                );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }
}
