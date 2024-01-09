package org.project.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.shoppingmall.domain.Address;
import org.project.shoppingmall.domain.User;
import org.project.shoppingmall.dto.request.SignupRequestDto;
import org.project.shoppingmall.exception.Duplication;
import org.project.shoppingmall.repository.AddressRepository;
import org.project.shoppingmall.repository.UserRepository;
import org.project.shoppingmall.type.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    // TODO : 이메일 인증, 인증 번호 확인, 닉네임 중복 확인
    @Transactional
    public Long signup(SignupRequestDto signupRequestDto) {
        User user = User.createUser(signupRequestDto.getNickname(), signupRequestDto.getEmail(),
                signupRequestDto.getPassword(), signupRequestDto.getPhoneNumber());

        User userSave = userRepository.save(user);

        Address address = Address.createAddress(signupRequestDto.getPostCode(), signupRequestDto.getMainAddress(),
                signupRequestDto.getDetailAddress(), user);

        addressRepository.save(address);

        log.debug("회원가입 성공");

        return userSave.getId();
    }

    // 닉네임 중복 확인
    public void duplicationNickname(String nickname) {
         userRepository.findByNickname(nickname).ifPresent(a -> {
            throw new Duplication(ErrorMessage.NICK_NAME_DUPLICATION);
        });
    }

    // 이메일 중복 확인
    public void duplicationEmail(String email) {
        userRepository.findByEmail(email).ifPresent(a -> {
            throw new Duplication(ErrorMessage.EMAIL_DUPLICATION);
        });
    }

}
