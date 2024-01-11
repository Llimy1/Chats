package org.project.chats.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.User;
import org.project.chats.dto.SecurityUserDto;
import org.project.chats.exception.JwtException;
import org.project.chats.repository.UserRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request Header에서 accessToken을 가져온다.
        String accessToken = request.getHeader("Authorization");

        // 토큰 검사 생략 (모두 허용 URL의 경우 토큰 검사 통과)
        if (!StringUtils.hasText(accessToken)) {
            doFilter(request, response, filterChain);
            return;
        }

        // AccessToken을 검증하고, 만료되었을경우 예외를 발생시킨다.
        if (!jwtUtil.verifyToken(accessToken)) {
            throw new JwtException(ErrorMessage.JWT_EXPIRATION);
        }

        // AccessToken의 값이 있고, 유효한 경우에 진행한다.
        if (jwtUtil.verifyToken(accessToken)) {

            // AccessToken 내부의 payload에 있는 email로 user를 조회한다. 없다면 예외 발생 -> 정상 토큰이 아님
            User user = userRepository.findByEmail(jwtUtil.getEmail(accessToken))
                    .orElseThrow(() -> new JwtException(ErrorMessage.JWT_NOT_NORMAL_TOKEN));

            // SecurityContext에 등록할 User 객체를 만들어준다.
            SecurityUserDto userDto = SecurityUserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole())
                    .build();

            // SecurityContext에 인증 객체 등록
            Authentication authentication = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    public Authentication getAuthentication(SecurityUserDto securityUserDto) {
        return new UsernamePasswordAuthenticationToken(securityUserDto, null,
                List.of(new SimpleGrantedAuthority(securityUserDto.getRole().getDescription())));
    }
}
