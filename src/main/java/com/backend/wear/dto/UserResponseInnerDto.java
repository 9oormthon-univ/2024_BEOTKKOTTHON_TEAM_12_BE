package com.backend.wear.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class UserResponseInnerDto {
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MyPageDto{
        private String userName; //사용자 이름
        private String nickName;
        private String universityName;
        private List<String> style; //스타일 태그 이름 리스트
        private String[] profileImage;
        private String level;
        private String nextLevel;
        private Integer point;
        private Integer remainLevelPoint;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProfileDto{
        private String userName; //사용자 이름
        private String nickName;
        private List<String> style; //스타일 태그 이름 리스트
        private String[] profileImage;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InfoDto{
        private String userName;
        private String universityName;
        private String universityEmail;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellerDto{
        private Long id; // pk
        private String nickName;
        private String[] profileImage;
        private String level;
    }
}
