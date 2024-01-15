package org.project.chats.type;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NICK_NAME_DUPLICATION("닉네임 중복 입니다."),
    EMAIL_DUPLICATION("이메일 중복 입니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    OAUTH_PROVIDER_NOT_FOUND("해당 소셜 로그인 제공자를 찾을 수 없습니다."),
    JWT_EXPIRATION("토큰 시간이 만료 되었습니다."),
    JWT_NOT_NORMAL_TOKEN("정상적인 토큰이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND("해당 액세스 토큰으로 리프레쉬 토큰을 찾을 수 없습니다."),
    TOKEN_REISSUE_FAIL("토큰 재발급에 실패 했습니다."),
    LOGIN_EMAIL_OR_PASSWORD_INACCURATE("아이디 또는 비밀번호가 일치하지 않습니다.");

    private final String description;

    ErrorMessage(String description) {
        this.description = description;
    }
}
