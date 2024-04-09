package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name="recommend_place")
@Entity
public class RecommendPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대학 이름
    @Column(name="place_name")
    private String placeName;

    // 대학교 (대학 별 거래 장소 추천)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    University university;

//    // 대학 이름
//    @Column(name="university_name")
//    private String universityName;

}
