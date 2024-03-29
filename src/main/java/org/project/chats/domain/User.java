package org.project.chats.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.chats.type.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;


    @Builder
    public User(String nickname, String email, String password, String phoneNumber, Role role, String provider) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.provider = provider;
    }

    public static User createUser(String nickname, String email, String password, String phoneNumber) {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
    }

    public static User createSocialUser(String nickname, String email, String phoneNumber, String provider) {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .provider(provider)
                .build();
    }

    public String getRoleDescription() {
        return this.role.getDescription();
    }

    public void passwordEncoder(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
}
