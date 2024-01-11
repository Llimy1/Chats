package org.project.chats.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private final String nickname;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final String postCode;
    private final String mainAddress;
    private final String detailAddress;

    @Builder
    public SignupRequestDto(String nickname, String email, String password, String phoneNumber, String postCode, String mainAddress, String detailAddress) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.mainAddress = mainAddress;
        this.detailAddress = detailAddress;
    }
}
