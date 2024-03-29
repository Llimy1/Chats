package org.project.chats.type;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    SIGNUP_SUCCESS("회원가입에 성공 했습니다."),
    SOCIAL_LOGIN_SIGNUP_SUCCESS("소셜 로그인 - 회원가입에 성공 했습니다."),
    LOGIN_SUCCESS("로그인에 성공 했습니다."),
    LOGOUT_SUCCESS("로그아웃에 성공 했습니다."),
    TOKEN_REISSUE_SUCCESS("토큰 재발급에 성공 했습니다."),
    CHECK_NICKNAME_SUCCESS("사용 가능한 닉네임 입니다."),
    CHECK_EMAIL_SUCCESS("사용 가능한 이메일 입니다."),
    CHAT_ROOM_CREATE_SUCCESS("채팅방 개설에 성공 했습니다."),
    CHAT_ROOM_SELECT_SUCCESS("채팅방 조회에 성공 했습니다."),
    CHAT_ROOM_ID_SELECT_SUCCESS("채팅방 아이디 조회에 성공 했습니다."),
    CHAT_MESSAGE_SELECT_SUCCESS("저장된 채팅 조회에 성공 했습니다."),
    REDIS_CHAT_ROOM_INSERT_INFO_DELETE_SUCCESS("레디스에 채팅방 정보 삭제에 성공 했습니다."),
    CONNECT_SERVER_SIDE_EVENT_SUCCESS("SSE 연결에 성공 했습니다.");

    private final String description;

    SuccessMessage(String description) {
        this.description = description;
    }
}
