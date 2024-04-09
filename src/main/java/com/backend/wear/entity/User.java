package com.backend.wear.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert //save할 때 null 값을 배제하고 insert
@DynamicUpdate
@Table(name="users")
@Entity
public class User extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //사용자 로그인 아이디
    @NotNull
    @Column(name="user_created_id")
    private String userCreatedId;;

    //사용자 패스워드
    @NotNull
    @Column(name="user_password")
    private String userPassword;

    //이름 (실명)
    @NotNull
    @Column(name="user_name")
    private String userName;

    //닉네임
    @NotNull
    @Column(name="nick_name")
    private String nickName;

    //대학교 이메일
    @Column(name="university_email")
    private String universityEmail;

    //환경 점수
    @Min(value = 0)
    @Max(value = 500)
    @Column(name="point",columnDefinition = "integer default 0")
    private Integer point;

    //환경 레벨
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="level", columnDefinition = "varchar(10) default 'SEED'")
    private EnvironmentLevel level;

    //스타일
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Style> style = new ArrayList<>();

    //프로필 이미지
    @Column(name="profile_image")
    private String profileImage;

    //대학교
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    University university;

    //판매 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

    //기부 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Donation> donationList=new ArrayList<>();

    //기부 신청 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DonationApply> donationApplyList=new ArrayList<>();

    //찜목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wish> wishList=new ArrayList<>();

//    //채팅방
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ChatRoom> chatRoomList=new ArrayList<>();

    // 스타일 필드를 갱신하는 메서드
    public void updateStyle(List<Style> newStyleList) {
        this.style.clear(); // 현재 스타일 목록 비우기
        this.style.addAll(newStyleList); // 새로운 스타일 목록 추가
    }

    //차단한 사용자 이름
//    @Column(name="blocked_user_name")
//    private List <String> blockedUserNameList=new ArrayList<>();
}