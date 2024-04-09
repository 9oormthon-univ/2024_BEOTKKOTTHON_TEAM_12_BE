package com.backend.wear.dto;

import com.backend.wear.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponseDto{

    //상품 조회 시 응답  dto
    private Long id;  //상품 아이디

    private Integer price;

    //   @JsonProperty(value="product_name")
    private String productName;

    //   @JsonProperty(value="product_status")
    private String productStatus;

    //  @JsonProperty(value="post_status")
    private String postStatus;

    //   @JsonProperty(value="product_image")
    private String[] productImage;

    private Boolean isSelected;

    private UserResponseDto seller; //판매자

    //    @JsonProperty(value="product_content")
    private String productContent;

    private String place; //거래 장소

    private Boolean isPrivate;


    public ProductResponseDto(Product product) {
        this.id = (long) product.getId(); // Product entity의 id는 int 타입이므로 형변환 필요
        this.price = product.getPrice();
        this.productName = product.getProductName();
        // productStatus, postStatus, productContent, place, isPrivate 등의 정보는 Product entity에 없으므로 여기에서는 생략
        this.isSelected = product.isPrivate(); // 기본값으로 false 설정
        this.productImage = new String[]{product.getProductImage()}; // String 배열로 변환하여 설정
        // seller는 UserResponseDto를 이용하여 매핑
        this.seller = new UserResponseDto(
                product.getUser().getId(),
                product.getUser().getUserName(),
                product.getUser().getNickName(),
                product.getUser().getUniversityEmail(),
                null,
                null,
                product.getUser().getProfileImage(),
                null,
                null, // Next level은 여기서 설정하지 않음
                product.getUser().getPoint(),
                null // Remain level point는 여기서 설정하지 않음
        );
    }
}