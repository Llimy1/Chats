package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.service.CommonService;
import org.project.chats.service.NotificationService;
import org.project.chats.type.SuccessMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.project.chats.type.SuccessMessage.CONNECT_SERVER_SIDE_EVENT_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequestMapping("/notification")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final CommonService commonService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@RequestParam(name = "userName") String userName,
                                            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        log.info("userName = {}", userName);
        SseEmitter emitter = notificationService.subscribe(userName, lastEventId);

        return ResponseEntity.status(OK).body(emitter);
    }
}