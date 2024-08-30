package com.sparta.spartaeats.entity;

import com.sparta.spartaeats.types.OrderStatus;
import com.sparta.spartaeats.types.OrderType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_orders")
public class Order extends TimeStamped{

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProductList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private Integer order_price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delvr_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;




}
