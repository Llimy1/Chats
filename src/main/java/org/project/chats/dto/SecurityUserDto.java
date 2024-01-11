package org.project.chats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.chats.type.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUserDto {

    private Long id;
    private String nickname;
    private String email;
    private String phoneNumber;
    private Role role;

    @Builder
    public SecurityUserDto(Long id, String nickname, String email, String phoneNumber, Role role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
