package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.dto.request.SignupRequestDto;
import org.project.chats.service.CommonService;
import org.project.chats.service.SignupService;
import org.project.chats.type.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
                commonService.successResponse(SuccessMessage.SIGNUP_SUCCESS.getDescription(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping("/check/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> checkNickname(@RequestParam String nickname) {
        log.debug("닉네임 중복 확인 API 호출");
        signupService.duplicationNickname(nickname);
        CommonResponseDto<Object> commonResponse =
                commonService.successResponse(SuccessMessage.CHECK_NICKNAME_SUCCESS.getDescription(), null);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/check/email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> checkEmail(@RequestParam String email) {
        log.debug("이메일 중복 확인 API 호출");
        signupService.duplicationEmail(email);
        CommonResponseDto<Object> commonResponse =
                commonService.successResponse(SuccessMessage.CHECK_EMAIL_SUCCESS.getDescription(), null);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }
}
