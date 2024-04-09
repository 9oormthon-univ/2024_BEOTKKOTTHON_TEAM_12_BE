package com.backend.wear.controller;

import com.backend.wear.dto.DonationApplyRequestDto;
import com.backend.wear.service.DonationApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donations")
public class DonationApplyController {
    private final DonationApplyService donationApplyService;

    @Autowired
    public DonationApplyController(DonationApplyService donationApplyService){
        this.donationApplyService=donationApplyService;
    }

    // 기부 등록하기
    // api/donations/{userId}?charity={charity}
    @PostMapping("/{userId}")
    public ResponseEntity<?> postDonationApply(@PathVariable("userId")Long userId,@RequestParam Integer charity,
                                               @RequestBody DonationApplyRequestDto donationApplyRequestDto){
            System.out.println(donationApplyRequestDto.getUserName());
        System.out.println(donationApplyRequestDto.getEmail());

        try{
            donationApplyService.donationApplyService(userId, charity, donationApplyRequestDto);
            return ResponseEntity.ok().body("기부가 완료되었습니다.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
