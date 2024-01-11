package org.project.shoppingmall.type;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    SIGNUP_SUCCESS("회원가입에 성공 했습니다."),
    CHECK_NICKNAME_SUCCESS("사용 가능한 닉네임 입니다."),
    CHECK_EMAIL_SUCCESS("사용 가능한 이메일 입니다.");

    private final String description;

    SuccessMessage(String description) {
        this.description = description;
    }
}
