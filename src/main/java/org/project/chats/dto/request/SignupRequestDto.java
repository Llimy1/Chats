package org.project.chats.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;

    @Builder
    public SignupRequestDto(String nickname, String email, String password, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
