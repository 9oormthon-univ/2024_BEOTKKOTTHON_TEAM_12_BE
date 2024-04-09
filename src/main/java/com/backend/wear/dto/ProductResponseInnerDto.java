
package com.backend.wear.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ProductResponseInnerDto{

    // 상품 상세 dto
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DetailDto{
        private Long id;  //상품 아이디

        private UserResponseInnerDto.SellerDto seller; //판매자

        private Integer price;

        private String productName;

        private String productStatus;

        private String postStatus;

        private String productContent;

        private String[] productImage;

        private String place; // 거래 장소

        private String createdTime; // 상품 등록 시간

        private String time; // 몇분 전
    }

    // 상품 썸네일
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenDto{
        private Long id;  //상품 아이디

        private Integer price;

        private String productName;

        private String productStatus;

        private String postStatus;

        private String[] productImage;

        private Boolean isSelected;

        //      private LocalDateTime createdAt;
        private String time;
    }

    // 마이페이지 상품 내역
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MyPageScreenDto{
        private Long id;  //상품 아이디

        private Integer price;

        private String productName;

        private String productStatus;

        private String postStatus;

        private String[] productImage;

        //    private LocalDateTime createdAt;

        private String time;
    }

    // 숨김 상품
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PrivateDto{
        private Long id;  //상품 아이디

        private Integer price;

        private String productName;

        private String productStatus;

        private String postStatus;

        private String[] productImage;

        private boolean isPrivate;

        private String time;
    }
}