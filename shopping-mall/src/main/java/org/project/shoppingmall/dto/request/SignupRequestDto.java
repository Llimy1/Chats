package org.project.shoppingmall.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private final String nickname;
    private final String email;
    private final String password;
    private final String postCode;
    private final String mainAddress;
    private final String detailAddress;
    private final String phoneNumber;

    @Builder
    public SignupRequestDto(String nickname, String email, String password, String postCode, String mainAddress, String detailAddress, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.postCode = postCode;
        this.mainAddress = mainAddress;
        this.detailAddress = detailAddress;
        this.phoneNumber = phoneNumber;
    }
}
