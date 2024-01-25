package org.project.chats.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.project.chats.domain.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    boolean existsByUserId(Long userId);

    @Query("select count(cu) from ChatRoomUser cu" +
            " join cu.chatRoom cr" +
            " where cr.roomId = :roomId")
    int countChatRoomUserByRoomId(@Param("roomId") String roomId);
}
