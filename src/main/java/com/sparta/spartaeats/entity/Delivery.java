package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_delivery")
public class Delivery extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "delvr_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private String zip;
    private String address;
    private String local;
    private String address2;
    private String contact;
    private Character delYn;
    private Character useYn;


}
