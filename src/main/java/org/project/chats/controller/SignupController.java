package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.dto.request.SignupRequestDto;
import org.project.chats.dto.request.SocialLoginRequestDto;
import org.project.chats.service.CommonService;
import org.project.chats.service.SignupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.project.chats.type.SuccessMessage.CHECK_EMAIL_SUCCESS;
import static org.project.chats.type.SuccessMessage.CHECK_NICKNAME_SUCCESS;
import static org.project.chats.type.SuccessMessage.SIGNUP_SUCCESS;
import static org.project.chats.type.SuccessMessage.SOCIAL_LOGIN_SIGNUP_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;
    private final CommonService commonService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> signup(@RequestBody SignupRequestDto signupRequestDto) {
        log.debug("회원가입 API 호출");
        Long userId = signupService.signup(signupRequestDto);
        CommonResponseDto<Object> commonResponse =
                commonService.successResponse(SIGNUP_SUCCESS.getDescription(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @PostMapping("/signup/social")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> socialSignup(@RequestBody SocialLoginRequestDto socialLoginRequestDto) {
        log.debug("소셜 로그인 - 회원가입 API 호출");
        Long userId = signupService.socialLogin(socialLoginRequestDto);
        CommonResponseDto<Object> commonResponseDto =
                commonService.successResponse(SOCIAL_LOGIN_SIGNUP_SUCCESS.getDescription(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }

    @GetMapping("/check/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> checkNickname(@RequestParam String nickname) {
        log.debug("닉네임 중복 확인 API 호출");
        signupService.duplicationNickname(nickname);
        CommonResponseDto<Object> commonResponse =
                commonService.successResponse(CHECK_NICKNAME_SUCCESS.getDescription(), null);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/check/email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> checkEmail(@RequestParam String email) {
        log.debug("이메일 중복 확인 API 호출");
        signupService.duplicationEmail(email);
        CommonResponseDto<Object> commonResponse =
                commonService.successResponse(CHECK_EMAIL_SUCCESS.getDescription(), null);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }
}
