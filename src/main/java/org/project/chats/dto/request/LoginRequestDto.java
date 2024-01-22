package org.project.chats.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String email;
    private String password;

    @Builder
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
