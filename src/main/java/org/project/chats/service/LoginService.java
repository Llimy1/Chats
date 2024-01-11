package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.User;
import org.project.chats.dto.GeneratedTokenDto;
import org.project.chats.dto.request.LoginRequestDto;
import org.project.chats.exception.LoginException;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 자체 로그인
    @Transactional
    public GeneratedTokenDto login(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            // 설정한 비밀번호 인코더를 사용해서 비밀번호 검증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            log.debug("아이디와 비밀번호 검증");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("SecurityContext add Authentication = {}", authentication);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
            log.debug("유저를 찾아옴");
            return jwtUtil.generatedToken(email, user.getRoleDescription());
        } catch (Exception e) {
            throw new LoginException(ErrorMessage.LOGIN_EMAIL_OR_PASSWORD_INACCURATE);
        }
    }
}
