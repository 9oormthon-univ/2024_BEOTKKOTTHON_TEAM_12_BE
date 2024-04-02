package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.entity.*;
import com.backend.wear.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    private final DonationApplyRepository donationApplyRepository;

    private final WishRepository wishRepository;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(UserRepository userRepository, StyleRepository styleRepository,
                       UniversityRepository universityRepository, DonationApplyRepository donationApplyRepository,
                       WishRepository wishRepository, ProductRepository productRepository, ObjectMapper objectMapper){
        this.userRepository=userRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
        this.donationApplyRepository=donationApplyRepository;
        this.wishRepository=wishRepository;
        this.productRepository=productRepository;
        this.objectMapper = objectMapper;
    }

    // List<String>를 JSON 문자열로 변환하는 메서드
    private String convertImageListToJson(List<String> imageList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageList);
    }

    // JSON 문자열을 List<String>으로 변환하는 메서드
    private List<String> convertJsonToImageList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<String>>() {});
    }

    // 마이페이지 사용자 정보
    @Transactional
    public UserResponseDto.MyPageDto getMyPageUserService(Long userId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        System.out.println("유저"+user.getUserName());

        return mapToMyPageDto(user, userId);
    }

    // 마이페이지 응답 dto 매핑
    private UserResponseDto.MyPageDto mapToMyPageDto(User user, Long userId) throws Exception{
        String universityName = getUniversityNameByUser(userId); //대학 이름
        List<String> style = getUserStyleList(userId); //스타일 리스트
        String level=getCurrentLevel(userId); //현재 레벨
        String nextLevel=getNextLevel(level); //다음 레벨
        Integer point=getPoint(userId);
        Integer remainLevelPoint= getRemainLevelPoint(point);

        System.out.println("이름: "+user.getUserName());

        String json = objectMapper.readValue(user.getProfileImage(), String.class);
        List<String> imageList = convertJsonToImageList(json);

        return UserResponseDto.MyPageDto.builder()
                .userName(user.getNickName())
                .nickName(user.getNickName())
                .universityName(universityName)
                .style(style)
                .profileImage(imageList)
                .level(level)
                .nextLevel(nextLevel)
                .point(point)
                .remainLevelPoint(remainLevelPoint)
                .build();
    }

    // 마이페이지 프로필
    @Transactional
    public UserResponseDto.ProfileDto getUserProfileService(Long userId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToProfileDto(user, userId);
    }

    // 마이페이지 프로필 응답 dto 매핑
    private UserResponseDto.ProfileDto mapToProfileDto(User user, Long userId) throws Exception {
        List<String> styleList = getUserStyleList(userId); //스타일 리스트

        String json = objectMapper.readValue(user.getProfileImage(), String.class);
        List<String> imageList = convertJsonToImageList(json);

        return UserResponseDto.ProfileDto.builder()
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .profileImage(imageList)
                .style(styleList)
                .build();
    }

    // 마이페이지 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserRequestDto.ProfileDto profileDto) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserName(profileDto.getUserName());
        user.setNickName(profileDto.getNickName());

        // List<String>을 JSON 문자열로 변환
        String profileImage = convertImageListToJson(profileDto.getProfileImage());

        user.setProfileImage(profileImage);

        setProfileStyle(user,profileDto.getStyle());
    }

    // 유저 정보 조회
    @Transactional
    public UserResponseDto.InfoDto getUserInfoService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToInfoDto(user,userId);
    }

    // 사용자 정보 info 응답 dto
    private UserResponseDto.InfoDto mapToInfoDto(User user, Long userId){
        String universityName=getUniversityNameByUser(userId);

        return UserResponseDto.InfoDto.builder()
                .userName(user.getUserName())
                .universityName(universityName)
                .universityEmail(user.getUniversityEmail())
                .build();
    }

    // info 저장
    @Transactional
    public void updateUserInfoService(Long userId, UserRequestDto.InfoDto infoDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserName(infoDto.getUserName());
  //      user.setUniversityEmail(infoDto.getUniversityEmail());
  //      updateUniversityName(userId, infoDto.getUniversityName());
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, UserRequestDto.PasswordDto passwordDto){
        if (!passwordDto.getNewPassword().equals(passwordDto.getCheckPassword()))
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");

        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserPassword(passwordDto.getCheckPassword());
    }

    // 찜한 상품 불러오기
    @Transactional
    public List<ProductResponseDto.ScreenDto> getWishList(Long userId) throws Exception{
        // 찜 리스트
       List<Wish> wishList = wishRepository.findByUserId(userId);

        // 찜한 상품 리스트 dto로 변환
       List<ProductResponseDto.ScreenDto> productList = new ArrayList<>();

       if(wishList.isEmpty())
           throw new IllegalArgumentException("현재 찜한 상품이 없습니다.");

        for (Wish wish : wishList) {
            // 사용자가 찜한 상품
            Product product = wish.getProduct();

            String json = objectMapper.readValue(product.getProductImage(), String.class);
            List <String> imageList = convertJsonToImageList(json);

            // 사용자의 상품 찜 여부 확인
            boolean isSelected = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

            // DTO 생성 및 리스트에 추가
            productList.add(ProductResponseDto.ScreenDto.builder()
                    .id(product.getId())
                    .price(product.getPrice())
                    .productName(product.getProductName())
                    .productStatus(product.getProductStatus())
                    .postStatus(product.getPostStatus())
                    .productImage(imageList)
                    .isSelected(isSelected)
                    .createdAt(product.getCreatedAt())
                    .build());
        }

       return productList;
    }

//    //판매 중, 완료 상품 불러오기
//    @Transactional
//    public List<ProductResponseDto> myProductsService(Long userId, String postStatus){
//        List<ProductResponseDto> productResponseDtoList = mapToProductResponseDtoPostStatus(userId,postStatus);
//
//        if(productResponseDtoList.isEmpty()){
//            if(postStatus.equals("onSale"))
//                throw new IllegalArgumentException("현재 판매중인 상품이 없습니다.");
//            else
//                throw new IllegalArgumentException("현재 판매 완료한 상품이 없습니다.");
//        }
//
//        else
//            return productResponseDtoList;
//    }
//
//    @Transactional
//    public List<ProductResponseDto> myHistoryService(Long userId){
//        List<ProductResponseDto> list = mapToMyHistory(userId);
//
//        if(list.isEmpty())
//            throw new IllegalArgumentException("현재 구매한 상품이 없습니다.");
//
//        else
//            return list;
//    }

    //판매 중 상품 완료로 변경
    @Transactional
    public void postMyProductStatusService(Long userId, ProductRequestDto dto){
        Product product=productRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품 상태를 변경하는데 실패하였습니다.")  );

        product.setPostStatus(dto.getPostStatus());
  //      productRepository.save(product);
    }

    //숨김 처리 상품 보기
//    @Transactional
//    public List<ProductResponseDto> getMyProductsPrivateService(Long userId){
//        List<ProductResponseDto> privateList = mapToProductPostResponseDtoPrivate(userId);
//
//        if(privateList.isEmpty())
//            throw new IllegalArgumentException("현재 숨김 처리한 상품이 없습니다.");
//        else
//            return privateList;
//    }

    //내 기부 내역
    @Transactional
    public List<DonationApplyResponseDto> getMyDonationApplyService(Long userId){
        List<DonationApplyResponseDto> responseDtoList =mapToDonationApplyResponseDto(userId);

        if(responseDtoList.isEmpty())
            throw new IllegalArgumentException("현재 기부한 상품이 없습니다. 기부를 통해 환경을 도와주세요.");
        else
            return responseDtoList;
    }

    @Transactional
    public List<DonationApplyResponseDto> getMyDonationApplyCompleteService(Long userId){
        List<DonationApplyResponseDto> responseDtoList
                = mapToDonationApplyResponseDtoComplete(userId);
        if(responseDtoList.isEmpty()){
            throw new IllegalArgumentException("현재 기부 진행이 완료된 내역이 없습니다.");
        }

        else
            return responseDtoList;
    }

//    //판매 내역
//    private List<ProductResponseDto> mapToProductResponseDtoPostStatus(Long userId, String postStatus){
//        List<Product> productList= productRepository.findByUserId(userId);
//        List<ProductResponseDto> myProductList = new ArrayList<>();
//
//        for(Product p: productList){
//            //상품 판매 상태가 요청과 같은 상품 리스트만 반환
//
//            if(!p.getPostStatus().equals(postStatus))
//                continue;
//
//            String array;
//            try {
//                array = objectMapper.readValue(p.getProductImage(), String.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//
//            ProductResponseDto dto=ProductResponseDto.builder()
//                    .id(p.getId())
//                    .price(p.getPrice())
//                    .productName(p.getProductName())
//                    .productStatus(p.getProductStatus())
//                    .postStatus(p.getPostStatus())
//                    .productImage(array)
//                    .build();
//
//            myProductList.add(dto);
//        }
//
//        return myProductList;
//    }
//
//    //구매 내역
//    private List<ProductResponseDto> mapToMyHistory(Long userId){
//        List<Product> list =  productRepository.findSoldOutProductsByUserId(userId);
//        List<ProductResponseDto> myHistoryList=new ArrayList<>();
//
//        for(Product p: list){
//            // JSON 배열 파싱
//            String array;
//            try {
//                array = objectMapper.readValue(p.getProductImage(), String.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            ProductResponseDto dto=ProductResponseDto.builder()
//                    .id(p.getId())
//                    .price(p.getPrice())
//                    .productName(p.getProductName())
//                    .productStatus(p.getProductStatus())
//                    .postStatus(p.getPostStatus())
//                    .productImage(array)
//                    .build();
//
//            myHistoryList.add(dto);
//        }
//
//        return myHistoryList;
//    }
//
//    // 숨김내역
//    private List<ProductResponseDto> mapToProductPostResponseDtoPrivate(Long userId){
//        List<Product> productList = productRepository.findByUser_IdAndIsPrivateTrue(userId);
//        List<ProductResponseDto> privateProductList= new ArrayList<>();
//
//        for(Product p: productList){
//            //상품 판매 상태가 요청과 같은 상품 리스트만 반환
//
//            ProductResponseDto dto=ProductResponseDto.builder()
//                    .id(p.getId())
//                    .price(p.getPrice())
//                    .productName(p.getProductName())
//                    .productStatus(p.getProductStatus())
//                    .postStatus(p.getPostStatus())
//                    .productImage(p.getProductImage())
//                    .isPrivate(p.isPrivate())
//                    .build();
//
//            privateProductList.add(dto);
//        }
//
//        return privateProductList;
//    }

    //내 기부 내역 응답 dto
    private List<DonationApplyResponseDto> mapToDonationApplyResponseDto(Long userId){
        List<DonationApply> donationApplyList=donationApplyRepository.findByUserId(userId);
        List<DonationApplyResponseDto> responseDtoList = new ArrayList<>();

        for(int i=0;i<donationApplyList.size();i++){
            DonationApply donationApply = donationApplyList.get(i);
            String date= donationApply.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

            DonationApplyResponseDto dto= DonationApplyResponseDto.builder()
                    .id(donationApply.getId())
                    .date(date)
                    .clothesCount(donationApply.getClothesCount())
                    .fashionCount(donationApply.getFashionCount())
                    .isDonationComplete(donationApply.isDonationComplete())
                    .build();

            responseDtoList.add(dto);
        }

        return responseDtoList;
    }


    //기부 내역 중 기부 완료만 보기
    private List<DonationApplyResponseDto> mapToDonationApplyResponseDtoComplete(Long userId){
        List<DonationApply> donationApplyList=donationApplyRepository.findByUserId(userId);
        List<DonationApplyResponseDto> responseDtoList = new ArrayList<>();

        for(int i=0;i<donationApplyList.size();i++){
            DonationApply donationApply = donationApplyList.get(i);
            if(!donationApply.isDonationComplete())
                continue;
            String date= donationApply.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

            DonationApplyResponseDto dto= DonationApplyResponseDto.builder()
                    .id(donationApply.getId())
                    .date(date)
                    .clothesCount(donationApply.getClothesCount())
                    .fashionCount(donationApply.getFashionCount())
                    .isDonationComplete(donationApply.isDonationComplete())
                    .build();

            responseDtoList.add(dto);
        }

        return responseDtoList;
    }

    // 스타일 태그 이름으로 Style 저장
    @Transactional
    public void setProfileStyle (User user, List<String> style){
        List<Style> newStyles = new ArrayList<>();

        styleRepository.deleteAllByUserId(user.getId());

        for(String s: style){
            Style styleTag =new Style();
            styleTag.setUser(user);
            styleTag.setStyleName(s);

            newStyles.add(styleTag);
        }

        user.setStyle(newStyles);
    }

    //대학교 이름 조회
    private String getUniversityNameByUser(Long userId){
        return userRepository.findById(userId).get().
                getUniversity().getUniversityName();
    }

    //대학교 이름 변경 (일단 인증 절차X)
    private void updateUniversityName(Long userId, String universityName){

    //    University university = userRepository.findById(userId).get().getUniversity();

    //    university.setUniversityName(universityName);
    }

    //스타일 태그 이름만 조회
    private List<String> getUserStyleList(Long userId){
        List<Style> allStyle = styleRepository.findByUserId(userId);

        //Style 태그에서 styleName만 추출
        List<String> style = new ArrayList<>();

        for(Style s: allStyle){
            style.add(s.getStyleName());
        }

        return style;
    }

    //현재 레벨 조회
    private String getCurrentLevel(Long userId){
        return userRepository.findById(userId).get()
                .getLevel().getLabel();
    }

    // 다음 레벨
    private String getNextLevel(String currentLevel){
        if (currentLevel.equals("씨앗"))
            return "새싹";
        else if(currentLevel.equals("새싹"))
            return "목화";
        else if(currentLevel.equals("목화"))
            return "꽃";
        else if(currentLevel.equals("꽃"))
            return "옷";
        else
            return "레벨 달성 완료";
    }

    //현재 포인트
    private Integer getPoint(Long userId){
        return userRepository.findById(userId).get()
                .getPoint();
    }

    //남은 레벨 포인트
    private Integer getRemainLevelPoint(Integer currentPoint){
        if(currentPoint>=0 && currentPoint<100)
            return 100-currentPoint;
        else if(currentPoint>= 100 && currentPoint<200)
            return 200-currentPoint;
        else if(currentPoint>= 200 && currentPoint<300)
            return 300-currentPoint;
        else if(currentPoint>=300 && currentPoint<400)
            return 400-currentPoint;
        else
            return 500-currentPoint;
    }

}