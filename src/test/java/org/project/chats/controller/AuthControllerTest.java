package org.project.chats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.dto.request.LoginRequestDto;
import org.project.chats.dto.response.GeneratedTokenDto;
import org.project.chats.service.CommonService;
import org.project.chats.service.LoginService;
import org.project.chats.type.ResponseStatus;
import org.project.chats.type.SuccessMessage;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 컨트롤러 테스트")
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    private LoginRequestDto loginRequestDto() {
        return LoginRequestDto.builder()
                .email("abce@mail.com")
                .password("12345a@")
                .build();
    }

    @Test
    @DisplayName("[API] 로그인 컨트롤러 테스트 - 성공")
    void login_success() throws Exception {
        //given
        LoginRequestDto loginRequestDto = loginRequestDto();
        String body = objectMapper.writeValueAsString(loginRequestDto);

        GeneratedTokenDto generatedTokenDto =
                GeneratedTokenDto.builder()
                        .accessToken(ACCESS_TOKEN)
                        .refreshToken(REFRESH_TOKEN)
                        .build();

        CommonResponseDto<Object> commonResponseDto =
                CommonResponseDto.builder()
                        .status(ResponseStatus.SUCCESS.getDescription())
                        .message(SuccessMessage.LOGIN_SUCCESS.getDescription())
                        .data(generatedTokenDto)
                        .build();

        given(loginService.login(any())).willReturn(generatedTokenDto);
        given(commonService.successResponse(
                SuccessMessage.LOGIN_SUCCESS.getDescription(),
                generatedTokenDto)).willReturn(commonResponseDto);

        //when
        //then
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status")
                        .value(ResponseStatus.SUCCESS.getDescription()))
                .andExpect(jsonPath("$.message")
                        .value(SuccessMessage.LOGIN_SUCCESS.getDescription()))
                .andExpect(jsonPath("$.data.accessToken")
                        .value(generatedTokenDto.getAccessToken()))
                .andDo(print());
    }
}