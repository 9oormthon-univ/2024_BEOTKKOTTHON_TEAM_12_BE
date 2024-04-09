package com.backend.wear.controller;

import com.backend.wear.dto.*;
import com.backend.wear.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    // 마이페이지 사용자 정보
    //api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<?> getMyPageUser(@PathVariable(name="userId") Long userId) throws Exception {
        UserResponseInnerDto.MyPageDto myPageDto;

        try{
            myPageDto=userService.getMyPageUserService(userId);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(myPageDto);
    }

    // 사용자 프로필
    // api/users/profile/{userId}
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable(name="userId") Long userId) throws Exception{
        UserResponseInnerDto.ProfileDto profileDto;

        try{
            profileDto =userService.getUserProfileService(userId);

        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok(profileDto);
    }

    // 사용자 프로필 수정
    // api/users/profile/{userId}
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable(name="userId") Long userId,
                                               @RequestBody UserRequestDto.ProfileDto profileDto)
    throws Exception
    {
        try {
            userService.updateUserProfile(userId, profileDto);
            return ResponseEntity.ok().body("회원 정보가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 계정 정보
    // api/users/userInfo/{userId}
    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable(name="userId") Long userId){
        UserResponseInnerDto.InfoDto userResponseDto;

        try{
            userResponseDto=userService.getUserInfoService(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok(userResponseDto);
    }

    // 계정 정보 저장
    // api/users/userInfo/update/{userId}
    @PutMapping("/userInfo/update/{userId}")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name="userId") Long userId,@RequestBody UserRequestDto.InfoDto infodto){
        try {
            userService.updateUserInfoService(userId, infodto);
            return ResponseEntity.ok().body("사용자 이름이 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 비밀번호 변경하기
    // api/users/password/{userId}
    @PutMapping ("/password/{userId}")
    public ResponseEntity<?> putPassword(@PathVariable(name="userId") Long userId, @RequestBody UserRequestDto.PasswordDto passwordDto){
        try {
            userService.updatePassword(userId, passwordDto);
            return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 찜한 상품 리스트 보기
    // api/users/wishList/{userId}
    @GetMapping("/wishList/{userId}")
    public ResponseEntity<?> getWishList(@PathVariable(name="userId") Long userId) throws Exception{
        try {
            List<ProductResponseInnerDto.ScreenDto> wishList = userService.getWishList(userId);
            return ResponseEntity.ok(wishList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 판매 중 상품 불러오기
    // api/users/myProducts/onSale/{userId}
    @GetMapping("/myProducts/onSale/{userId}")
    public ResponseEntity<?> getMyProductsOnSale(@PathVariable(name="userId") Long userId) throws Exception{
        try{
            List<ProductResponseInnerDto.MyPageScreenDto> myProductList =
                    userService.getMyProductsList(userId,"onSale");
            return ResponseEntity.ok(myProductList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 판매 중인 상품 판매 완료하기
    // api/users/myProducts/onSale/{userId}
    @PutMapping("/myProducts/onSale/{userId}")
    public ResponseEntity<?> putPostStatus(@PathVariable(name="userId") Long userId,
                                                 @RequestBody ProductRequestDto requestDto){
        try{
            userService.changePostStatus(userId, requestDto);
            return ResponseEntity.ok().body("상품 판매가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 판매 완료 상품 불러오기
    // api/users/myProducts/soldOut/{userId}
    @GetMapping("/myProducts/soldOut/{userId}")
    public ResponseEntity<?> getMyProductsSoldOut(@PathVariable(name="userId") Long userId) throws Exception{
        try{
            List<ProductResponseInnerDto.MyPageScreenDto> myProductList =
                    userService.getMyProductsList(userId,"soldOut");
            return ResponseEntity.ok(myProductList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 숨김 처리 상품 불러오기
    // api/users/myProducts/private/{userId}
    @GetMapping("/myProducts/private/{userId}")
    public ResponseEntity<?> getMyProductsPrivate(@PathVariable(name="userId") Long userId) throws Exception{

        try{
            List<ProductResponseInnerDto.PrivateDto> privateList = userService.myyProductsPrivateList(userId);
            return ResponseEntity.ok(privateList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 내 기부 내역 불러오기
    // api/users/myDonations/{userId}
    @GetMapping ("/myDonations/{userId}")
    public ResponseEntity<?> getMyDonationApply(@PathVariable(name="userId") Long userId){
        try {;
            List <DonationApplyResponseDto> responseDtoList
                    =userService.myDonationApplyList(userId);
            return ResponseEntity.ok().body(responseDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 내 기부 내역 중 기부 완료 된 상품만 불러오기
    // api/users/complete/{userId}
    @GetMapping ("/myDonations/complete/{userId}")
    public ResponseEntity<?> getMyDonationCompleteApplyComplete(@PathVariable(name="userId") Long userId){
        try {;
            List <DonationApplyResponseDto> responseDtoList
                    =userService.myDonationApplyCompleteList(userId);
            return ResponseEntity.ok().body(responseDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 구매내역
//    @GetMapping("/myHistory/{userId}")
//    public ResponseEntity<?> getMyHistory(Long userId){
//        try {;
//            List<ProductResponseDto> list=userService.myHistoryService(userId);
//            return ResponseEntity.ok().body(list);
//
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(e.getMessage());
//        }
//    }
}
