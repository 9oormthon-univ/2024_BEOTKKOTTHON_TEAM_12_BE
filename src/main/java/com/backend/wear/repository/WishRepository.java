package com.backend.wear.repository;

import com.backend.wear.entity.Product;
import com.backend.wear.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    //사용자가 찜한 상품
    Optional<Wish> findByUserIdAndProductId(Long userId, Long productId);
    List<Wish> findByUserId(Long userId);
}
