package org.project.shoppingmall.type;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    NICK_NAME_DUPLICATION("닉네임 중복 입니다."),
    EMAIL_DUPLICATION("이메일 중복 입니다.");

    private final String description;

    ErrorMessage(String description) {
        this.description = description;
    }
}
