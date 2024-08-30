package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_AI")
public class AI extends TimeStamped{
    
    @Id
    @GeneratedValue
    @Column(name = "ai_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String requestContents;
    private String responseContents;
    private Character delYn;

}
