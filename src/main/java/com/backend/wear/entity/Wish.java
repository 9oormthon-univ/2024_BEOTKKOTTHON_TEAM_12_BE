package com.backend.wear.entity;

import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Table(name="wish")
@Entity
public class Wish extends Serializers.Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    // 찜 여부
//    @Column(name="is_selected",columnDefinition = "boolean default false")
//    private boolean isSelected;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Builder
    public Wish(User user, Product product){
        this.user=user;
        this.product=product;
    }
}