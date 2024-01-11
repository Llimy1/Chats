package org.project.chats.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("패스워드 암호화 테스트")
public class PasswordEncoderTest {

    @Test
    @DisplayName("패스워드 암호화 테스트 - 성공")
    void password_encode_success() {
        //given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "12345";

        //when
        String encodedPassword = passwordEncoder.encode(password);

        //then
        assertThat(password).isNotEqualTo(encodedPassword);
        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
    }
}
