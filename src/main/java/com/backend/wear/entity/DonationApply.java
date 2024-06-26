package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Table(name="donation_apply")
@Entity
public class DonationApply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name="user_id")
    private User user; //기부 신청자

    //기부 단체
    @Column(name="charity_number")
    private Integer charityNumber;

    //신청자 이름
    @Column(name="user_name")
    private String userName;

    //방문 주소
    private String address;

    //휴대 전화
    private String phone;

    //이메일 주소
    private String email;

    //기부 품목
    @Column(name="donation_item")
    private String donationItem;

    //의류 수량
    @Column(name = "clothes_count", columnDefinition = "integer default 0")
    private Integer clothesCount;

    //잡화 수량
    @Column(name="fashion_count", columnDefinition = "integer default 0")
    private Integer fashionCount;

    //박스 수량
    @Column(name="box_count", columnDefinition = "integer default 0")
    private Integer boxCount;

    //기부 상태, 완료/미완료
    @Column(name="is_donation_complete", columnDefinition = "boolean default false")
    private boolean isDonationComplete;
}