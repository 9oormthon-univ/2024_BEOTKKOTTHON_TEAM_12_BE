package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DonationApplyRequestDto {
    private String userName; //기부 신청자 이름

    private String address;

    private String phone;

    private String email;

    private String donationItem; //기부 품목

    private Integer clothesCount; //의류 수량

    private Integer fashionCount; //잡화 수량

    private Integer boxCount; //박스 개수
}