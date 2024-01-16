package org.project.chats.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SocialLoginRequestDto {

    private String nickname;
    private String email;
    private String phoneNumber;
    private String provider;

    @Builder
    public SocialLoginRequestDto(String nickname, String email, String phoneNumber, String provider) {
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.provider = provider;
    }
}
