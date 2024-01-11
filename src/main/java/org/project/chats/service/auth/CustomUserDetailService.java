package org.project.chats.service.auth;

import lombok.RequiredArgsConstructor;
import org.project.chats.domain.User;
import org.project.chats.exception.NotFoundException;
import org.project.chats.repository.UserRepository;
import org.project.chats.type.ErrorMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        return CustomUserDetails.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRoleKey())
                .build();
    }
}
