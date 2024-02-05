package org.project.chats.repository;

import org.project.chats.domain.ChatRoomRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRedisRepository extends CrudRepository<ChatRoomRedis, String> {

    Optional<ChatRoomRedis> findByRoomIdAndEmail(String roomId, String email);

    List<ChatRoomRedis> findAllByRoomId(String roomId);
}
