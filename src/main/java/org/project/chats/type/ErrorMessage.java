package org.project.chats.type;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NICK_NAME_DUPLICATION("닉네임 중복 입니다."),
    EMAIL_DUPLICATION("이메일 중복 입니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    OAUTH_PROVIDER_NOT_FOUND("해당 소셜 로그인 제공자를 찾을 수 없습니다."),
    JWT_EXPIRATION("토큰 시간이 만료 되었습니다."),
    JWT_NOT_NORMAL_TOKEN("정상적인 토큰이 아닙니다.");

    private final String description;

    ErrorMessage(String description) {
        this.description = description;
    }
}
