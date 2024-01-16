package org.project.chats.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeneratedTokenDto {

    private String accessToken;
    private String refreshToken;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Builder
    public GeneratedTokenDto(String accessToken, String refreshToken) {
        this.accessToken = TOKEN_PREFIX + accessToken;
        this.refreshToken = TOKEN_PREFIX + refreshToken;
    }
}
