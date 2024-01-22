package org.project.chats.repository;

import org.project.chats.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    boolean existsByUserId(Long userId);
}
