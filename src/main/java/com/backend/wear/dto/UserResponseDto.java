
package com.backend.wear.dto;

import com.backend.wear.entity.Style;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//사용자에 대한 응답 dto
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDto {
    private Long id; //pk
    private String userName; //사용자 이름
    private String nickName;
    private String universityEmail;
    private String universityName;
    private List<String> style; //스타일 태그 이름 리스트
    private String profileImage;
    private String level;
    private String nextLevel;
    private Integer point;
    private Integer remainLevelPoint;
}