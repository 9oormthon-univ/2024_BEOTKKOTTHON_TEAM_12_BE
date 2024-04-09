package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class UserRequestDto {
    private Long id; //pk
    private String userName; //사용자 이름
    private String nickName;
    private  String[] profileImage;
    private List<String> style; //스타일 태그 이름 리스트
    private String universityEmail;
    private String universityName;
    private String level;

    // 프로필 dto
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProfileDto {
        private String userName;
        private String nickName;
        String[] profileImage;
        private List<String> style;
    }

    // info dto
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InfoDto {
        String userName;
        String universityName;
        String universityEmail;
    }

    // 비밀번호 변경 dto
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PasswordDto{
        private String newPassword;
        private String checkPassword;
    }
}