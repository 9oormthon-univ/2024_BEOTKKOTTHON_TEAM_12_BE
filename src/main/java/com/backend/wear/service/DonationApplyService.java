package com.backend.wear.service;

import com.backend.wear.dto.DonationApplyRequestDto;
import com.backend.wear.entity.DonationApply;
import com.backend.wear.entity.User;
import com.backend.wear.repository.DonationApplyRepository;
import com.backend.wear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationApplyService {

    private final UserRepository userRepository;
    private final DonationApplyRepository donationApplyRepository;

    @Autowired
    public DonationApplyService(DonationApplyRepository donationApplyRepository,
                                UserRepository userRepository){
        this.donationApplyRepository=donationApplyRepository;
        this.userRepository=userRepository;
    }

    // 기부 신청
    public void donationApplyService(Long userId, Integer charityNumber, DonationApplyRequestDto dto){
        // 아이디로 사용자 조회 (기부 신청자)
        User applyUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        DonationApply donationApply= DonationApply.builder()
                .user(applyUser)
                .charityNumber(charityNumber)
                .userName(dto.getUserName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .donationItem(dto.getDonationItem())
                .clothesCount(dto.getClothesCount())
                .fashionCount(dto.getFashionCount())
                .boxCount(dto.getBoxCount())
                .isDonationComplete(false)
                .build();

        donationApplyRepository.save(donationApply);

        // 환경 점수 추가
        applyUser.setPoint(applyUser.getPoint()+5);
    }
}
