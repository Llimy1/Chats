package org.project.shoppingmall.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.shoppingmall.domain.Address;
import org.project.shoppingmall.domain.User;
import org.project.shoppingmall.dto.request.SignupRequestDto;
import org.project.shoppingmall.exception.Duplication;
import org.project.shoppingmall.repository.AddressRepository;
import org.project.shoppingmall.repository.UserRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {


    @Mock
    UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    SignupService signupService;

    private SignupRequestDto signupRequestDto;

    @BeforeEach
    void setUp() {
        signupRequestDto = SignupRequestDto.builder()
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
    @DisplayName("[Service] 회원 가입 서비스 - 성공")
    void signup_success() {
        // given
        User user = User.createUser(
                signupRequestDto.getNickname(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getPhoneNumber());

        given(userRepository.save(any()))
                .willReturn(user);

        ReflectionTestUtils.setField(user, "id", 1L);

        Address address = Address.createAddress(
                signupRequestDto.getPostCode(),
                signupRequestDto.getMainAddress(),
                signupRequestDto.getDetailAddress(),
                user);

        given(addressRepository.save(any()))
                .willReturn(address);

        ReflectionTestUtils.setField(address, "id", 1L);

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