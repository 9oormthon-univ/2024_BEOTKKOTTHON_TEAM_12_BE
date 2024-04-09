package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Table(name="blocked_users")
@Entity
public class BlockedUser extends BaseEntity { //차단된 사용자

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //차단한 사용자 pk
    @NotNull
    @Column(name="blocked_user_id",unique = true)
    private Long blockedUserId;

    // 차단하기 버튼을 눌러 다른 사용자를 차단한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

//    //차단한 사용자 로그인 아이디
//    @NotNull
//    @Column(name = "blocked_created_id", unique = true)
//    private String blockedUserCreatedId;
//
//    //닉네임
//    @NotNull
//    @Column(name = "blocked_nick_name", unique = true)
//    private String blockedNickName;
//
//    //환경 레벨
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Column(name = "level")
//    private EnvironmentLevel blockedLevel;
//
//    //환경 점수
//    @Min(value = 0)
//    @Column(name = "point")
//    private Integer blockedPoint;
//
//    //프로필 이미지
//    @Column(name = "profile_image")
//    private String blockedProfileImage;
}