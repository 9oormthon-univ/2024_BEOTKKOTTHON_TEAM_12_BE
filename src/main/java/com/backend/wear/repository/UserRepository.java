package com.backend.wear.repository;

import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 //   List<ChatRoom> findChatRoomListByUserId(Long userId);

    Optional<User> findByUserCreatedId(String userCreatedId);

}