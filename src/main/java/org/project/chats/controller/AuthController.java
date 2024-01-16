package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.dto.request.LoginRequestDto;
import org.project.chats.dto.response.GeneratedTokenDto;
import org.project.chats.service.CommonService;
import org.project.chats.service.LoginService;
import org.project.chats.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.project.chats.type.SuccessMessage.LOGIN_SUCCESS;
import static org.project.chats.type.SuccessMessage.LOGOUT_SUCCESS;
import static org.project.chats.type.SuccessMessage.TOKEN_REISSUE_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final RefreshTokenService tokenService;
    private final CommonService commonService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.debug("로그인 API 호출");
        GeneratedTokenDto generatedTokenDto = loginService.login(loginRequestDto);

        log.debug("redis에 토큰 저장");
        tokenService.saveTokenInfo(
                loginRequestDto.getEmail(),
                generatedTokenDto.getAccessToken(),
                generatedTokenDto.getRefreshToken());

        CommonResponseDto<Object> commonResponseDto = commonService.successResponse(
                LOGIN_SUCCESS.getDescription(),
                        generatedTokenDto.getAccessToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }

    @PostMapping("/token/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String accessToken) {
        log.debug("로그아웃 API 호출");
        tokenService.removeRefreshToken(accessToken);
        CommonResponseDto<Object> commonResponseDto = commonService.successResponse(
                LOGOUT_SUCCESS.getDescription(),
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<Object> reissue(@RequestHeader("Authorization") String accessToken) {
        log.debug("토큰 재발급 API 호출");
        log.info("controller accessToken = {}", accessToken);
        String newAccessToken = tokenService.reissueToken(accessToken);
        log.info("new accessToken = {}", newAccessToken);

       CommonResponseDto<Object> commonResponseDto = commonService.successResponse(
               TOKEN_REISSUE_SUCCESS.getDescription(),
               newAccessToken
       );
       return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }
}
