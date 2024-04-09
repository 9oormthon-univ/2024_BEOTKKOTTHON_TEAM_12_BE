package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
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
    @Column(name = "clothes_count")
    private Integer clothesCount;

    //잡화 수량
    @Column(name="fashion_count")
    private Integer fashionCount;

    //박스 수량
    @Column(name="box_count")
    private Integer boxCount;

    //기부 상태, 완료/미완료
    @Column(name="is_donation_complete", columnDefinition = "boolean default false")
    private boolean isDonationComplete;

    @Builder
    public DonationApply(User user, Integer charityNumber, String userName, String address, String phone,
                         String email, String donationItem, Integer clothesCount, Integer fashionCount, Integer boxCount,
                         boolean isDonationComplete){
        this.user=user;
        this.charityNumber=charityNumber;
        this.userName=userName;
        this.address=address;
        this.phone=phone;
        this.email=email;
        this.donationItem=donationItem;
        this.clothesCount=clothesCount;
        this.fashionCount=fashionCount;
        this.boxCount=boxCount;
        this.isDonationComplete=isDonationComplete;
    }
}