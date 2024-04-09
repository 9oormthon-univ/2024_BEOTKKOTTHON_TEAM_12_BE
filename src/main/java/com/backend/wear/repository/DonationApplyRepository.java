package com.backend.wear.repository;

import com.backend.wear.entity.DonationApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DonationApplyRepository extends JpaRepository<DonationApply, Long> {

    //사용자 아이디로 기부 내역 찾기
    List <DonationApply> findByUserId(Long userId);

    // 기부 완료 내역
    @Modifying
    @Transactional
    @Query("SELECT d FROM DonationApply d WHERE d.user.id = :userId AND d.isDonationComplete = true")
    List<DonationApply> findByUserIdAndDonationComplete(Long userId);
}