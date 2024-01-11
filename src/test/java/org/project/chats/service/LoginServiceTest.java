package org.project.chats.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.chats.domain.User;
import org.project.chats.dto.GeneratedTokenDto;
import org.project.chats.dto.request.LoginRequestDto;
import org.project.chats.exception.LoginException;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.project.chats.type.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DisplayName("로그인 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    private User user;

    private final String PREFIX = "Bearer ";
    private final String ACCESS_TOKEN = "accessToken";
    private final String REFRESH_TOKEN = "refreshToken";

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        user = User.builder()
                .nickname("abc")
                .email("abce@mail.com")
                .password("12345a")
                .phoneNumber("01011111111")
                .role(Role.USER)
                .build();
        user.passwordEncoder(passwordEncoder);
    }

    @Test
    @DisplayName("[Service] 로그인 서비스 테스트 - 성공")
    void login_success() {

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("abce@mail.com")
                .password("12345a")
                .build();


        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // given
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(authenticationManager.authenticate(any()))
                .willReturn(new UsernamePasswordAuthenticationToken(email, password));
        given(jwtUtil.generatedToken(anyString(), anyString()))
                .willReturn(new GeneratedTokenDto(ACCESS_TOKEN, REFRESH_TOKEN));
        //when
        GeneratedTokenDto result = loginService.login(loginRequestDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(PREFIX + ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(PREFIX + REFRESH_TOKEN);
        verify(userRepository).findByEmail(email);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(email, password));
        verify(jwtUtil).generatedToken(email, Role.USER.getDescription());
    }

    @Test
    @DisplayName("[Service] 로그인 서비스 테스트 - 실패")
    void login_fail() {

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("abce@mail.com")
                .password("12345")
                .build();


        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // given
        given(authenticationManager.authenticate(any()))
                .willThrow(new LoginException(ErrorMessage.LOGIN_EMAIL_OR_PASSWORD_INACCURATE));

        //when
        //then
        assertThatThrownBy(() -> loginService.login(loginRequestDto))
                .isInstanceOf(LoginException.class);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}