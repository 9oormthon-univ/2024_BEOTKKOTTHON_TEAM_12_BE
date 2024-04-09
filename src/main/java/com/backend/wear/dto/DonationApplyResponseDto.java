package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DonationApplyResponseDto {

    private Long id; //기부 pk

    private String date; //기부 날짜

    private Integer clothesCount; //의류 개수

    private Integer fashionCount; //잡화 개수

    private Boolean isDonationComplete; //기부 상태 (true, false)
}