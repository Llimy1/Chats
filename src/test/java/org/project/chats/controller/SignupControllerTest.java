package org.project.chats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.dto.request.SignupRequestDto;
import org.project.chats.service.CommonService;
import org.project.chats.service.SignupService;
import org.project.chats.type.ResponseStatus;
import org.project.chats.type.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    @MockBean
    private SignupService signupService;

    @MockBean
    private CommonService commonService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupRequestDto signupRequestDto() {
        return SignupRequestDto.builder()
                .nickname("min")
                .email("abcd@mail.com")
                .password("12345a@@")
                .phoneNumber("01011111111")
                .postCode("12345")
                .mainAddress("서울시")
                .detailAddress("집")
                .build();
    }

    @Test
    @DisplayName("[API] 회원 가입 컨트롤러 - 성공")
    void signup_success() throws Exception {
        //given
        SignupRequestDto signupRequestDto = signupRequestDto();
        String body = objectMapper.writeValueAsString(signupRequestDto);
        Long userId = 1L;

        CommonResponseDto<Object> commonResponseDto =
                CommonResponseDto.builder()
                        .status(ResponseStatus.SUCCESS.getDescription())
                        .message(SuccessMessage.SIGNUP_SUCCESS.getDescription())
                        .data(userId)
                        .build();

        given(signupService.signup(any())).willReturn(userId);
        given(commonService.successResponse(
                SuccessMessage.SIGNUP_SUCCESS.getDescription(),
                userId
        )).willReturn(commonResponseDto);

        //when
        //then
        mvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status")
                        .value(ResponseStatus.SUCCESS.getDescription()))
                .andExpect(jsonPath("message")
                        .value(SuccessMessage.SIGNUP_SUCCESS.getDescription()))
                .andExpect(jsonPath("data")
                        .value(userId))
                .andDo(print());
    }

    @Test
    @DisplayName("[API] 닉네임 사용 가능 - 성공")
    void duplication_nickname_success() throws Exception {

        CommonResponseDto<Object> commonResponseDto =
                CommonResponseDto.builder()
                        .status(ResponseStatus.SUCCESS.getDescription())
                        .message(SuccessMessage.CHECK_NICKNAME_SUCCESS.getDescription())
                        .data(null)
                        .build();

        //given
        given(commonService.successResponse(
                SuccessMessage.CHECK_NICKNAME_SUCCESS.getDescription(),
                null
        )).willReturn(commonResponseDto);

        //when
        //then
        mvc.perform(get("/check/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nickname", "nickname"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(ResponseStatus.SUCCESS.getDescription()))
                .andExpect(jsonPath("$.message")
                        .value(SuccessMessage.CHECK_NICKNAME_SUCCESS.getDescription()))
                .andDo(print());
    }

    @Test
    @DisplayName("[API] 이메일 사용 가능 - 성공")
    void duplication_email_success() throws Exception {

        CommonResponseDto<Object> commonResponseDto =
                CommonResponseDto.builder()
                        .status(ResponseStatus.SUCCESS.getDescription())
                        .message(SuccessMessage.CHECK_EMAIL_SUCCESS.getDescription())
                        .data(null)
                        .build();

        //given
        given(commonService.successResponse(
                SuccessMessage.CHECK_EMAIL_SUCCESS.getDescription(),
                null
        )).willReturn(commonResponseDto);

        //when
        //then
        mvc.perform(get("/check/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", "email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value(ResponseStatus.SUCCESS.getDescription()))
                .andExpect(jsonPath("$.message")
                        .value(SuccessMessage.CHECK_EMAIL_SUCCESS.getDescription()))
                .andDo(print());
    }
}