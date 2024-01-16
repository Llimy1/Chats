package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.RefreshToken;
import org.project.chats.exception.JwtException;
import org.project.chats.repository.RefreshTokenRepository;
import org.project.chats.service.jwt.JwtUtil;
import org.project.chats.type.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    private static final String PREFIX = "Bearer ";

    @Transactional
    public void saveTokenInfo(String email, String accessToken, String refreshToken) {
        refreshTokenRepository.save(RefreshToken.builder()
                .id(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {

        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND.getDescription()));

        log.debug("remove refreshToken = {}", refreshToken);
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public String reissueToken(String accessToken) {
        Optional<RefreshToken> findToken = refreshTokenRepository.findByAccessToken(accessToken);

        // RefreshToken이 존재하고 유효하다면 실행
        if (findToken.isPresent() && jwtUtil.verifyToken(findToken.get().getRefreshToken())) {
            log.info("if문 들어옴");
            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = findToken.get();
            log.info("권한과 아이디를 추출해 새로운 액세스토큰을 만든다.");
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = PREFIX + jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            log.info("액세스 토큰의 값을 수정해준다.");
            resultToken.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(resultToken);

            return newAccessToken;
        } else {
            throw new JwtException(ErrorMessage.TOKEN_REISSUE_FAIL);
        }
    }
}
