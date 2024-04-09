package com.backend.wear.repository;

import com.backend.wear.entity.BlockedUser;
import com.backend.wear.entity.Style;
import com.backend.wear.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedUserRepository extends JpaRepository<User, Long> {
 //   List<BlockedUser> findByUserId(Long userId);

    // userId로 차단한 사용자들의 pk blockedUserId 리스트 찾기
    @Query("SELECT b.blockedUserId FROM BlockedUser b WHERE b.user.id=:userId")
    List<Long> findByBlockedUserId(@Param("userId") Long userId);
}