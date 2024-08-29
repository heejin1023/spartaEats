package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_products")
public class Product extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private String productName;
    private Integer price;
    private String productDescription;

    private Character useYn;
    private Character delYn;
}
