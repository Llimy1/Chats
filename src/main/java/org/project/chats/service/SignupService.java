package org.project.chats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.domain.User;
import org.project.chats.dto.request.SignupRequestDto;
import org.project.chats.exception.Duplication;
import org.project.chats.repository.UserRepository;
import org.project.chats.type.ErrorMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(SignupRequestDto signupRequestDto) {
        User user = User.createUser(signupRequestDto.getNickname(), signupRequestDto.getEmail(),
                signupRequestDto.getPassword(), signupRequestDto.getPhoneNumber());
        user.passwordEncoder(passwordEncoder);

        User userSave = userRepository.save(user);

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
