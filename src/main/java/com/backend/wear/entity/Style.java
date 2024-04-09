package com.backend.wear.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert //save할 때 null 값을 배제하고 insert
@DynamicUpdate
@Table(name="style")
@Entity
public class Style extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //스타일 태그 이름
    @Column(name="style_name")
    private String styleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    User user;

    public Style(String styleName){
        this.styleName=styleName;
    }
}