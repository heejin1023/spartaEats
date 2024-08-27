package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_category")
public class Category extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private UUID id;
    private String categoryName;
    private String categoryDescription;
    private Character useYn;
    private Character delYn;

}
