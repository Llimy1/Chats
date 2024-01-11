package org.project.chats.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.chats.domain.User;
import org.project.chats.dto.request.SignupRequestDto;
import org.project.chats.exception.Duplication;
import org.project.chats.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("회원 가입 컨트롤러 테스트")
@ExtendWith(MockitoExtension.class)
class SignupServiceTest {


    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    SignupService signupService;

    private SignupRequestDto signupRequestDto;

    @BeforeEach
    void setUp() {

        passwordEncoder = new BCryptPasswordEncoder();

        signupRequestDto = SignupRequestDto.builder()
                .nickname("min")
                .email("abcd@mail.com")
                .password("12345a@@")
                .phoneNumber("01011111111")
                .build();
    }

    @Test
    @DisplayName("[Service] 회원 가입 서비스 - 성공")
    void signup_success() {
        // given
        User user = User.createUser(
                signupRequestDto.getNickname(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getPhoneNumber());
        user.passwordEncoder(passwordEncoder);

        given(userRepository.save(any()))
                .willReturn(user);

        ReflectionTestUtils.setField(user, "id", 1L);

        // when
        Long userId = signupService.signup(signupRequestDto);

        // then
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("[Service] 닉네임 중복")
    void duplication_nickname() {

        //given
        User user = User.createUser(
                signupRequestDto.getNickname(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getPhoneNumber());

        given(userRepository.findByNickname(signupRequestDto.getNickname()))
                .willReturn(Optional.of(user));


        //when
        //then
        assertThatThrownBy(() -> signupService.duplicationNickname(signupRequestDto.getNickname()))
                .isInstanceOf(Duplication.class);
    }
    @Test
    @DisplayName("[Service] 이메일 중복")
    void duplication_email() {

        //given
        User user = User.createUser(
                signupRequestDto.getNickname(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getPhoneNumber());

        given(userRepository.findByEmail(signupRequestDto.getEmail()))
                .willReturn(Optional.of(user));


        //when
        //then
        assertThatThrownBy(() -> signupService.duplicationEmail(signupRequestDto.getEmail()))
                .isInstanceOf(Duplication.class);
    }
}