package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyMessage {
    private Long id;

    private Long chatRoomId;

    private String message;
}