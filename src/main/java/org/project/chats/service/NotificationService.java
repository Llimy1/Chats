package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.Notification;
import org.project.chats.domain.User;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.EmitterRepository;
import org.project.chats.repository.NotificationRepository;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static org.project.chats.type.ErrorMessage.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 29분
    private static final Long DEFAULT_TIME_OUT = 1000L * 60 * 29;
    private static final String PREFIX_URL = "http://localhost:8080/";

    // SSE 연결
    @Transactional
    public SseEmitter subscribe(String userName, String lastEventId) {

        // 멤버 찾기
        User findUser = userRepository.findByNickname(userName)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Long findUserId = findUser.getId();

        // Emitter Id
        String emitterId = findUserId + "_" + System.currentTimeMillis();

        // Emitter Id와 29분의 타임아웃을 가진 emitter를 생성 후 map에 저장
        SseEmitter saveEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIME_OUT));

        log.info("new Emitter = {}", saveEmitter);

        // 상황 별 emitter 삭제
        // 완료
        saveEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        // 타임아웃
        saveEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지한 더미 데이터 전송
        send(saveEmitter, emitterId, "Event Stream Created. [memberId =" + findUserId +"]");

        // 클라이언트가 미수신한 Event 목록이 존재를 할 경우 전송하여 Event 유실 방지
        if (!lastEventId.isEmpty()) {

            Map<String, Object> eventList =
                    emitterRepository.findAllEventCacheStartWithId(String.valueOf(findUserId));

            // 미수신한 Event 목록 전송
            eventList.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) > 0)
                    .forEach(entry -> send(saveEmitter, entry.getKey(), entry.getValue()));
        }
        return saveEmitter;
    }

    private void send(SseEmitter sseEmitter, String emitterId, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sse")
                    .data(data)
                    .build());
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.error("SSE 연결 오류", e);
        }
    }

    @Transactional
    public void chatNotification(String content, String resource, Boolean isRead, User sender, User receiver) {
        log.info("chat notification");
        Notification chatNotification =
                Notification.createNotification(content, PREFIX_URL + resource, isRead, "chat", sender, receiver);

        // SseEmitter 캐시 조회를 위해 key의 prefix 생성
        String id = String.valueOf(chatNotification.getReceiver().getId());
        // 알림 저장
        notificationRepository.save(chatNotification);
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        log.info("Ssemitters = {}", sseEmitters);
        sseEmitters.forEach(
                (key, emitter) -> {
                    log.info("key ={}, emitter = {}", key, emitter);
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, chatNotification);
                    // 데이터 전송
                    send(emitter, key, chatNotification);
                    log.info("emitter = {}", emitter);
                }
        );

    }

}
