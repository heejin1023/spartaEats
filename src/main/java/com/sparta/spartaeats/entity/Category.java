package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_store_category")
@NoArgsConstructor
public class Category extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private UUID id;
    private String categoryName;
    private String categoryDescription;
    private Character useYn;
    private Character delYn;

    public Category(String categoryName, String categoryDescription, Character useYn, Character delYn) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.useYn = useYn;
        this.delYn = delYn;
    }
}
